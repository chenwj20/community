package cn.cwj.community.interceptor;


import cn.cwj.community.model.User;
import cn.cwj.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2020/1/14
 * @Version V1.0
 **/
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    //判断用户是否登入
                    User user = userService.findByToken(token);
                    if (user != null){
                        if (user.getStatus() == 1){

                            request.getSession().removeAttribute("user");
                            Cookie cookie1 = new Cookie("token", null);
                            cookie1.setMaxAge(0);
                            cookie1.setPath("/");
                            response.addCookie(cookie1);
                            response.sendRedirect("/user/ban");
                        }else {
                            request.getSession().setAttribute("user",user);
                        }
                    }
                    break;
                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
