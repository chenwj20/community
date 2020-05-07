package cn.cwj.community.controller;

import cn.cwj.community.cache.HotTagCache;
import cn.cwj.community.cache.HotUserCache;
import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.model.Category;
import cn.cwj.community.model.Question;
import cn.cwj.community.service.CategoryService;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Date 2020/2/1
 * @Version V1.0
 **/
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HotUserCache hotUserCache;
    private Integer pageSize = 20;
    @GetMapping("/{type}/page/{pageNum}")
    public String category(Model model,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable Integer type
                         ){

        PageInfo<QuestionDTO> pageInfo = questionService.questionList(pageNum, pageSize, type,null);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("type",type);
        //拼接url
        StringBuffer newSb = new StringBuffer("/category/");
        newSb.append(type);
        newSb.append("/new/page/");
        newSb.append(pageNum);

        StringBuffer hotSb = new StringBuffer("/category/");
        hotSb.append(type);
        hotSb.append("/hot/page/");
        hotSb.append(pageNum);

        model.addAttribute("newUrl",newSb.toString());
        model.addAttribute("hotUrl",hotSb.toString());

        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("hotTags",hotTags);
        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/category/");
        pageUrl.append(type);
        pageUrl.append("/page/");
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("pageUrl",pageUrl);
        return "jie/index";
    }

    @GetMapping("/{type}/{sort}/page/{pageNum}")
    public String category1(Model model,
                           @PathVariable("pageNum") Integer pageNum,
                           @PathVariable Integer type,
                            @PathVariable String sort
    ){

        PageInfo<QuestionDTO> pageInfo = questionService.questionList(pageNum, pageSize, type,sort);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("type",type);
        model.addAttribute("sort",sort);
        //拼接url
        StringBuffer newSb = new StringBuffer("/category/");
        newSb.append(type);
        newSb.append("/new/page/");
        newSb.append(pageNum);

        StringBuffer hotSb = new StringBuffer("/category/");
        hotSb.append(type);

        List<String> hotTags = hotTagCache.getHots();
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        model.addAttribute("commentCountUsers",commentCountUsers);
        hotSb.append("/hot/page/");
        hotSb.append(pageNum);
        model.addAttribute("newUrl",newSb.toString());
        model.addAttribute("hotUrl",hotSb.toString());
        model.addAttribute("hotTags",hotTags);
        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("commentCountQuestions",commentCountQuestions);

        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/category/");
        pageUrl.append(type);
        pageUrl.append("/");
        pageUrl.append(sort);

        pageUrl.append("/page/");
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("pageUrl",pageUrl);
        return "jie/index";
    }
}
