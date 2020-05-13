package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.tImer.CodeTimer;
import cn.cwj.community.util.EmailUtils;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Date 2020/2/1
 * @Version V1.0
 **/
@Controller
public class VerifyController {
    @RequestMapping("/imageCode")
    public void verifyImage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 40, 4, 20);
        captcha.write(response.getOutputStream());

        HttpSession session = request.getSession();
        session.removeAttribute("verifyCode");
        session.setAttribute("verifyCode",captcha.getCode());
    }
    @RequestMapping("/sendEmail")
    @ResponseBody
    public ResultDTO sendEmail(String email,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        String emailCode = EmailUtils.achieveCode();
        session.setAttribute("emailCode", emailCode);
        long mailTime = System.currentTimeMillis();
        CodeTimer codeTimer = new CodeTimer(session,"emailCode");
        codeTimer.timer.schedule(codeTimer,1000*60*5);
//        session.setAttribute("mailTime", mailTime);
        Boolean aBoolean = EmailUtils.sendAuthCodeEmail(email, emailCode);
        if (aBoolean){
            return ResultDTO.okOf(200,"发送成功");
        }else {
            return ResultDTO.errorOf(0,"发送失败");
        }

    }

    @PostMapping("/system/password")
    @ResponseBody
    public ResultDTO del(String password){
        if ("cwj6868".equals(password)){
            return ResultDTO.okOf(CommonEnum.PASSWORD_TRUE);
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_CODE_ERROR);
        }
    }

}
