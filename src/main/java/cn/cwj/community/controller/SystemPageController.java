package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.SystemUser;
import cn.cwj.community.service.SystemUserService;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Date 2020/3/3
 * @Version V1.0
 **/
@Controller
@RequestMapping("/system")
public class SystemPageController {


    @GetMapping("/login")
    public String login(){
        return "system/login";
    }

    @GetMapping("/userlist")
    public String userList(){
        return "system/table";
    }

    @GetMapping("/systemUserlist")
    public String systemUserlist(){
        return "system/table_system_user";
    }
    @GetMapping("/systemRolelist")
    public String systemRolelist(){
        return "system/table_system_role";
    }
    @GetMapping("/userEdit")
    public String userEdit(){
        return "system/edit";
    }

    @GetMapping("/questionMore")
    public String questionMore(){
        return "system/question-more";
    }
    @GetMapping("/questionList")
    public String questionList(){
        return "system/table-question";
    }
    @GetMapping("/commentList")
    public String commentList(){
        return "system/table-comment";
    }
    @GetMapping("/category")
    public String categoryList(){
        return "system/table-category";
    }
}
