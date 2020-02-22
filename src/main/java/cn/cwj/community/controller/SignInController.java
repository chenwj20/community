package cn.cwj.community.controller;

import cn.cwj.community.dto.IsSignInDTO;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.User;
import cn.cwj.community.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.PriorityQueue;

/**
 * @Date 2020/1/29
 * @Version V1.0
 * 签到
 **/
@RestController
@RequestMapping("/sign")
public class SignInController {
    @Autowired
    private SignInService signInService;

    /**
     * 用户签到状态
     * @param request
     * @return
     */
    @RequestMapping("/status")
    public ResultDTO signInStatus(HttpServletRequest request){
        User user= (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //判断用户是否签到
        IsSignInDTO signIn = signInService.isSignIn(user.getId());
        return ResultDTO.okOf(signIn);
    }

    /**
     * 用户签到
     * @param request
     * @return
     */
    @RequestMapping("/in")
    public ResultDTO isSignIn(HttpServletRequest request){
        User user= (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //签到
        IsSignInDTO isSignInDTO = signInService.toSingIn(user.getId());
        return ResultDTO.okOf(isSignInDTO);
    }
  /*  @RequestMapping("/list")
    public ResultDTO signIn(){

    }*/
}
