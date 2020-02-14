package cn.cwj.community.controller;

import cn.cwj.community.dto.AccessTokenDTO;
import cn.cwj.community.dto.GithubUser;
import cn.cwj.community.dto.QQAccessTokenDTO;
import cn.cwj.community.dto.QQUserDTO;
import cn.cwj.community.model.User;
import cn.cwj.community.provider.GithubProvider;
import cn.cwj.community.provider.QQProvider;
import cn.cwj.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private QQProvider qqProvider;
    @Autowired
    private UserService userService;
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Value("${qq.client.id}")
    private String qqClientId;

    @Value("${qq.client.secret}")
    private String qqClientSecret;

    @Value("${qq.redirect.uri}")
    private String qqRedirectUri;

    @GetMapping("/qqCallback")
    public String callbackQq(@RequestParam(name = "code") String code,
                             @RequestParam(name = "state") String state,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             Model model) {

        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO();
        qqAccessTokenDTO.setGrant_type("authorization_code");
        qqAccessTokenDTO.setCode(code);
        qqAccessTokenDTO.setClient_id(qqClientId);
        qqAccessTokenDTO.setClient_secret(qqClientSecret);
        qqAccessTokenDTO.setRedirect_uri(qqRedirectUri);
        // System.out.println("code是"+code+"state是"+state);
        String string = qqProvider.getAccessToken(qqAccessTokenDTO);
        String access_token = string.split("&")[0].split("=")[1];
        String openid = qqProvider.getOpenID(access_token);
        QQUserDTO qqUser = qqProvider.getUser(access_token,qqClientId,openid);
        if (qqUser != null && qqUser.getOpenId() != null) {
            User user = new User();
//            User userInfo = new User();
            String token = UUID.randomUUID().toString();
            user.setName(qqUser.getNickname());
            user.setToken(token);
            user.setAccountId(qqUser.getOpenId());
            user.setAvatarUrl(qqUser.getFigureurl_qq());
//            user.setGender(qqUser.getGender());
            user.setLocation(qqUser.getProvince()+"省"+qqUser.getCity()+"市");
            userService.createOrUpdateUser(user);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect: /";

        }else {
            log.error("callback get qq error,{}", qqUser);
            return "redirect:/";
        }
    }
    @GetMapping("/githubCallback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println("accessToken:"+accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println("user:"+githubUser);
        if (githubUser != null){
            request.getSession().setAttribute("user",githubUser);
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setBio(githubUser.getBto());
            userService.createOrUpdateUser(user);
            Cookie cookie = new Cookie("token", user.getToken());
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            //写入cookie
            response.addCookie(cookie);
            return "redirect:/";
            //登入成功
        }else {
            return "redirect:/";
        }

    }



}
