package cn.cwj.community.controller;

import cn.cwj.community.dto.AccessTokenDTO;
import cn.cwj.community.dto.GithubUser;
import cn.cwj.community.model.User;
import cn.cwj.community.provider.GithubProvider;
import cn.cwj.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/githubCallback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        System.out.println("code:"+code);
        System.out.println("state:"+state);

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
