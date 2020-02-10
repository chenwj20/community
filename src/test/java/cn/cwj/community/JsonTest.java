package cn.cwj.community;

import cn.cwj.community.dto.CommentDTO;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonTest {
    @Test
    public void test01(){
//定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
//CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
//图形验证码写出，可以写出到文件，也可以写出到流
        String code = captcha.getCode();
        System.out.println(code);
        captcha.write("f:/circle.png");
//验证图形验证码的有效性，返回boolean值
        boolean verify = captcha.verify(code);
        System.out.println(verify);
    }
}
