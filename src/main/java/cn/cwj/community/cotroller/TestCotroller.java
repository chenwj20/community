package cn.cwj.community.cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/
@Controller
@RequestMapping("test")
public class TestCotroller {
    @RequestMapping("index1")
    public String index(){


        return "index";
    }
}
