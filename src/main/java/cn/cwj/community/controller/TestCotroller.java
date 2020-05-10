package cn.cwj.community.controller;

import cn.cwj.community.schedule.HotUserTasks;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/
@Controller
@RequestMapping("test")
public class TestCotroller {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HotUserTasks hotUserTasks;
    @RequestMapping("index1")
    public String index(){


        return "index";
    }

    @RequestMapping("/password")
    @ResponseBody
    public String jiaPassword(String password){
        String encode = passwordEncoder.encode(password);
        return encode;
    }

    @RequestMapping("/hotq")
    @ResponseBody
    public String jcc(){
       hotUserTasks.hotQuestionSchedule();
       return "成功";
    }
}
