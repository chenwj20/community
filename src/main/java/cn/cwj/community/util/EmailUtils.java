package cn.cwj.community.util;

/**
 * @Date 2020/2/27
 * @Version V1.0
 **/
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.*;

@Component
public class EmailUtils {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    private Environment env;

    private static String auth;
    private static String host;
    private static String protocol;
    private static int port;
    private static String authName;
    private static String password;
    private static boolean isSSL;
    private static String charset ;
    private static String timeout;

    @PostConstruct
    public void initParam () {
        auth = env.getProperty("mail.smtp.auth");
        host = env.getProperty("mail.host");
        protocol = env.getProperty("mail.transport.protocol");
        port = env.getProperty("mail.smtp.port", Integer.class);
        authName = env.getProperty("mail.auth.name");
        password = env.getProperty("mail.auth.password");
        charset = env.getProperty("mail.send.charset");
        isSSL = env.getProperty("mail.is.ssl", Boolean.class);
        timeout = env.getProperty("mail.smtp.timeout");
    }


    /**
     * 发送邮件
     * @param subject 主题
     * @param toUsers 收件人
     * @param ccUsers 抄送
     * @param content 邮件内容
     * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
     */
    public static boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles) {
        boolean flag = true;
        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setUsername(authName);
            javaMailSender.setPassword(password);
            javaMailSender.setDefaultEncoding(charset);
            javaMailSender.setProtocol(protocol);
            javaMailSender.setPort(port);

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", auth);
            properties.setProperty("mail.smtp.timeout", timeout);
            if(isSSL){
                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                    sf.setTrustAllHosts(true);
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.ssl.socketFactory", sf);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
            javaMailSender.setJavaMailProperties(properties);

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(toUsers);
            if (ccUsers != null && ccUsers.length > 0) {
                messageHelper.setCc(ccUsers);
            }
            messageHelper.setFrom(authName);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            if (attachfiles != null && attachfiles.size() > 0) {
                for (Map<String, String> attachfile : attachfiles) {
                    String attachfileName = attachfile.get("name");
                    File file = new File(attachfile.get("file"));
                    messageHelper.addAttachment(attachfileName, file);
                }
            }
            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            logger.error("发送邮件失败!", e);
            flag = false;
        }
        return flag;
    }

    public static Boolean sendAuthCodeEmail(String userEmail, String authCode){
        String subject = "FreeMi官方邮箱验证码";
        String mailHtml = getMailHtml(authCode, new Date().toString());
        boolean b = sendEmail(subject, new String[]{userEmail}, null, mailHtml, null);
        return b;
    }

    /**
     * 邮件样式
     * @param code
     * @param date
     * @return
     */
    public static String getMailHtml(String code,String date){
        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"content\" style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">");
        sb.append("<div class=\"header\" style=\"MARGIN-BOTTOM: 30px\">");
        sb.append("<p>亲爱的用户您好：</p>");
        sb.append("</div><p>您好！您正在进行邮箱验证，本次请求的验证码为：</p>");
        sb.append("<p><span style=\"FONT-SIZE: 18px; FONT-WEIGHT: bold; COLOR: #f90\">");
        sb.append(code);
        sb.append("</span><span style=\"COLOR: #000000\">(为了保障您帐号的安全性，请在5分钟内完成验证)</span></p>");
        sb.append("<div class=\"footer\" style=\"MARGIN-TOP: 30px\">");
        sb.append("<p>FreeMi社区</p>");
        sb.append("<p><span style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">");
        sb.append(date);
        sb.append("</span></p></div><div class=\"tip\" style=\"COLOR: #cccccc; TEXT-ALIGN: center\">该邮件为系统自动发送，请勿进行回复 </div></div></div>");
        return sb.toString();

    }

    // 随机验证码
    public static String achieveCode() {  //由于数字1 和0 和字母 O,l 有时分不清，所有，没有字母1 、0
        String[] beforeShuffle= new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        List list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)); //将集合转化为字符串
        }
        return sb.toString().substring(3, 8);  //截取字符串第4到8
    }
}

