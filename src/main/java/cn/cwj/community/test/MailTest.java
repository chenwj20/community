package cn.cwj.community.test;


import cn.cwj.community.util.MailUtil;
import cn.hutool.extra.mail.MailAccount;


/**
 * @Date 2020/2/2
 * @Version V1.0
 **/
public class MailTest {
    public static void main(String[] args) {
     /*   MailAccount account = new MailAccount();
        account.setHost("smtp.163.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("chenweijun_yz@163.com");
        account.setUser("mikemhm");
        //密码(注意不是登录密码，是网易客户端登录授权码)
        account.setPass("123456cwj");*/
//        MailUtil.send("1834399097@qq.com", "测试", "邮件来自Hutool测试", false);
//        MailUtil.send("984770829@qq.com", "测试", "邮件来自Hutool测试", false);
        MailUtil.sendAuthCodeEmail("1834399097@qq.com","1234");
    }
}
