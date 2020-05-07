package cn.cwj.community.controller;

import cn.cwj.community.dto.SystemIndexDTO;
import cn.cwj.community.model.User;
import cn.cwj.community.service.CommentService;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Date 2020/3/3
 * @Version V1.0
 **/
@Controller
@RequestMapping("/system")
public class SystemIndexController {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @RequestMapping("/")
    public String index(HttpServletResponse response){
        return "system/index";
    }

    @RequestMapping("/welcome")
    public String welcome(Model model){
        int userCount = userService.findUserCount();
        int questionCount = questionService.findQuestionCount();
        int commentCount = commentService.findCommentCount();
        List<User> users = userService.findNewUsers();
        SystemIndexDTO systemIndexDTO = new SystemIndexDTO();
        systemIndexDTO.setCommentCount(commentCount);
        systemIndexDTO.setQuestionCount(questionCount);
        systemIndexDTO.setUserCount(userCount);
        systemIndexDTO.setUsers(users);
        model.addAttribute("info",systemIndexDTO);
        return "system/welcome";
    }

}
