package cn.cwj.community.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.http.HttpResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

/**
 * @Date 2020/2/1
 * @Version V1.0
 **/
public class VerifyUtil {

    public static boolean getCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        //CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("d:/circle.png");
        //验证图形验证码的有效性，返回boolean值
        boolean verify = captcha.verify("1234");
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        //Servlet的OutputStream记得自行关闭哦！
        outputStream.close();
        return verify;
    }
}
