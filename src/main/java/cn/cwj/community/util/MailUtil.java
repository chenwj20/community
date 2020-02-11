package cn.cwj.community.util;


import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Date 2020/2/2
 * @Version V1.0
 **/
public class MailUtil {

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
    public static Boolean sendAuthCodeEmail(String userEmail, String authCode) {
        HtmlEmail email=new HtmlEmail();//创建一个HtmlEmail实例对象

        email.setHostName("smtp.163.com");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            System.out.println("用户邮件为："+userEmail);
            email.setSmtpPort(25);
            email.addTo(userEmail);//设置收件人
            email.setFrom("chenweijun_yz@163.com","FreeMi");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("chenweijun_yz@163.com","123456cwj");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            email.setSubject("FreeMi官方邮箱验证码");//设置发送主题
            String mailHtml = getMailHtml(authCode, new Date().toString());
            System.out.println(email.getSmtpPort());
            email.setMsg(mailHtml);//设置发送内容
            email.send();//进行发送
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }

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

}
