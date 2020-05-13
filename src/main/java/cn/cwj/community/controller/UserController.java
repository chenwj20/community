package cn.cwj.community.controller;

import cn.cwj.community.dto.CollectionQuestionDTO;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.dto.UserOwnDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.exception.CustomizeException;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.User;
import cn.cwj.community.service.UserService;
import cn.hutool.crypto.digest.DigestUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/home/{id}")
    public String userHome(@PathVariable("id") Long id,
                           HttpServletRequest request,
                           Model model){
        User user = (User) request.getSession().getAttribute("user");

        UserDTO userDTO = userService.findByIdMore(id);
        UserOwnDTO userOwnDTO = null;
        if (user != null && user.getId().equals(id)){
            userOwnDTO = userService.findUserEx(userDTO);
        }
        model.addAttribute("userOwnDTO",userOwnDTO);
        model.addAttribute("userDTO",userDTO);
        return "user/home";
    }
    @GetMapping("/login")
    public String loginHtml(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user != null){
            return "redirect:/";
        }
        return "user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultDTO userLogin(User user,
                               @RequestParam("verifyCode") String verifyCode,
                               HttpServletRequest request,
                               HttpServletResponse response){
        HttpSession session = request.getSession();
        String code = (String)session.getAttribute("verifyCode");
        if (!verifyCode.equalsIgnoreCase(code)){
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_CODE_ERROR);
            }
            session.removeAttribute("verifyCode");
            String password = user.getPassword();
            String salt = password.substring(password.length() -2,password.length());
            //MD5加密
            String md5Password = DigestUtil.md5Hex(user.getPassword()+salt);
            User user1 = userService.findUserByEmailAndPassword(user.getEmail(), md5Password);
            if (user1 == null) {
                return ResultDTO.errorOf(CustomizeErrorCode.USER_VERIFY_ERROR);
            }
            String token = UUID.randomUUID().toString();
            user1.setToken(token);
            userService.updateUserToken(user1);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            session.setAttribute("user", user1);
            return ResultDTO.okOf();
        }

        @GetMapping("/logout")
        public String logout(HttpServletRequest request,
                         HttpServletResponse response){
            request.getSession().removeAttribute("user");
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user != null){
            return "redirect:/";
        }
        return "user/reg";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultDTO register(HttpServletRequest request,
                              HttpServletResponse response,
                              User user,
                              @RequestParam("emailCode") String emailCode,
                              @RequestParam("verifyCode") String verifyCode
                              ){
        HttpSession session = request.getSession();
        if (!verifyCode.equalsIgnoreCase((String) session.getAttribute("verifyCode"))){
            session.removeAttribute("verifyCode");
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFY_CODE_ERROR);
        }
        if (session.getAttribute("emailCode") == null){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_CODE_OVERDUE);
        }
        boolean isEmail = userService.checkUserByEmail(user);
        if (isEmail){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_IS_EXIST);
        }
        if (!emailCode.equalsIgnoreCase((String) session.getAttribute("emailCode"))){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_CODE_ERROR);
        }
//        Long time = new Date().getTime()-(Long) session.getAttribute("mailTime");
        session.removeAttribute("verifyCode");
        session.removeAttribute("emailCode");
        Boolean b = userService.checkUser(user);
        if (b){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EXIST);
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        cookie.setPath("/");
        response.addCookie(cookie);

        /*String password = user.getPassword();
        //盐密码后两位
        String salt = password.substring(password.length() -2,password.length());
        //MD5加密
        String md5Password = DigestUtil.md5Hex(user.getPassword()+salt);
        user.setPassword(md5Password);
        user.setGmtCreate(System.currentTimeMillis());
        user.setAccountType("freemi");
        */

        String accountId = token.replaceAll("-", "");
        user.setAccountId(accountId);
        userService.insertOrEdit(user);
        User userNew = userService.findByToken(token);
        session.setAttribute("user",userNew);
        return ResultDTO.okOf(200,"注册成功");
    }

    @GetMapping("/forget")
    public String forget(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user != null){
            return "redirect:/";
        }
        return "user/forget";
    }

    @PostMapping("/forget")
    @ResponseBody
    public ResultDTO forget(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "emailCode") String emailCode,
                            @RequestParam("email") String email
                         ){
        HttpSession session = request.getSession();

        if (session.getAttribute("emailCode") == null){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_CODE_OVERDUE);
        }
        if (!emailCode.equalsIgnoreCase((String) session.getAttribute("emailCode"))){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_CODE_ERROR);
        }
        session.removeAttribute("emailCode");
        User user = new User();
        user.setEmail(email);
        boolean isEmail = userService.checkUserByEmail(user);
        if (!isEmail){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_UNREG);
        }
        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        cookie.setPath("/");
        response.addCookie(cookie);
        user.setToken(token);
        userService.updateUserToken(user);
        User fuser = userService.finUserByEmail(email);
        session.setAttribute("user",fuser);

        return ResultDTO.okOf();
    }

    @GetMapping("/collection")
    public String myCollection(HttpServletRequest request,
                               Model model,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "8") Integer pageSize){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        PageInfo<CollectionQuestionDTO> myCollections = userService.findMyCollection(user.getId(), pageNum, pageSize);
        model.addAttribute("myCollections",myCollections);
        return "user/collection";
    }

    @RequestMapping("/question")
    public String myQuestion(HttpServletRequest request,
                             Model model,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "8") Integer pageSize){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        PageInfo<Question> myQuestions = userService.findMyQuestions(user.getId(), pageNum, pageSize);
        model.addAttribute("myQuestions",myQuestions);
        PageInfo<CollectionQuestionDTO> myCollections = userService.findMyCollection(user.getId(), pageNum, pageSize);
        model.addAttribute("myCollections",myCollections);
        return "user/index";
    }
    @GetMapping("/set")
    public String userSet(HttpServletRequest request,
                          Model model){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        User setUser = userService.findById(user.getId());
        model.addAttribute("user",setUser);
        return "user/set";
    }
    @PostMapping("/set")
    @ResponseBody
    public ResultDTO userSet(HttpServletRequest request,
                            @RequestParam(value = "nowPassword" ,required = false) String nowPassword,
                            @RequestParam(value = "rePassWord",required = false) String rePassWord,
                             User user){
        User suser = (User)request.getSession().getAttribute("user");
        if (suser == null){
          return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //查询用户当前密码是否正确
        if (StringUtils.isNoneBlank(user.getPassword()) && StringUtils.isNoneBlank(nowPassword)){
            if (!rePassWord.equals(user.getPassword())){
                return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_DIFFERENCE_EEROR);
            }
            String pattern = "^[a-z0-9A-Z]{6,16}$";
            boolean nowP = Pattern.matches(pattern, nowPassword);
            boolean newP = Pattern.matches(pattern, user.getPassword());
            if (!newP || !nowP){
                return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_FORMAT_EEROR);
            }
            String salt = nowPassword.substring(nowPassword.length() -2,nowPassword.length());
            //MD5加密
            String md5Password = DigestUtil.md5Hex(nowPassword+salt);
            User puser = userService.findUserByEmailAndPassword(user.getEmail(), md5Password);
            if (puser == null){
                return ResultDTO.errorOf(CustomizeErrorCode.NOW_PASSWORD_EEROR);
            }
            String newSalt = user.getPassword().substring(user.getPassword().length() -2,user.getPassword().length());
            //MD5加密
            String newMd5Password = DigestUtil.md5Hex(user.getPassword()+newSalt);
            user.setPassword(newMd5Password);

        }

        user.setId(suser.getId());
        if (StringUtils.isNoneBlank(user.getName())){
            Boolean b = userService.checkUser(user);
            if (b){
                return ResultDTO.okOf(CustomizeErrorCode.USER_IS_EXIST);
            }
        }

        userService.insertOrEdit(user);
        return ResultDTO.okOf(CommonEnum.SET_SUCCESS);
    }
    @PostMapping("/set/avatar")
    @ResponseBody
    public ResultDTO userSetAvatar(HttpServletRequest request,
                                   String avatar){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        user.setAvatarUrl(avatar);
        userService.insertOrEdit(user);
        return ResultDTO.okOf(CommonEnum.AVATAR_SUCCESS);
    }
    @RequestMapping("/ban")
    public String userBan(){
        return "user/ban";
    }
}
