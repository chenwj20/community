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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    private Integer pageSize = 16;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private HotUserCache hotUserCache;
    @GetMapping("/")
    public String index(Model model){
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        System.out.println(commentCountUsers);
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        PageInfo<QuestionDTO> pageInfo = questionService.questionList(1, 20, null,null);
        List<QuestionDTO> topList = questionService.findByStatus(1);
        model.addAttribute("topList",topList);
        model.addAttribute("newUrl","/all/new/page/"+1);
        model.addAttribute("hotUrl","/all/hot/page/"+1);
        List<String> hotTags = hotTagCache.getHots();
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("hotTags",hotTags);

        model.addAttribute("type",null);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
       }

    @GetMapping("/{kind}/page/{pageNum}")
    public String index(Model model,
                        @RequestParam(required = false) String keyword,
                        @PathVariable String kind,
                        @PathVariable("pageNum") Integer pageNum){
        PageInfo<QuestionDTO> pageInfo = null;
        String newUrl = "/"+kind+"/new/page/1";
        String hotUrl = "/"+kind+"/hot/page/1";
        String pageUrl = "/"+kind+"/page/";

        if ("all".equals(kind)){
            model.addAttribute("condition",null);
            pageInfo = questionService.questionList(pageNum, pageSize, null,null);
        }
        if ("search".equals(kind)){
            String keywordTrim = keyword.trim();
            String  condition = "?keyword="+keywordTrim;
            newUrl= newUrl+condition;
            hotUrl = hotUrl + condition;
            model.addAttribute("condition",condition);
            pageInfo = questionService.findQuestionByKeyword(pageNum, pageSize, keywordTrim,null);
        }
        model.addAttribute("pageUrl",pageUrl);
        List<String> hotTags = hotTagCache.getHots();
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("hotTags",hotTags);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("newUrl",newUrl);
        model.addAttribute("hotUrl",hotUrl);
        model.addAttribute("type",null);
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        model.addAttribute("pageUrl",pageUrl);
        return "jie/index";
    }


    @GetMapping("/{kind}/{sort}/page/{pageNum}")
    public String index(Model model,
                        @PathVariable("pageNum") Integer pageNum,
                        @PathVariable String kind,
                        @PathVariable("sort") String sort,
                        @RequestParam(required = false) String keyword){
        PageInfo<QuestionDTO> pageInfo = questionService.questionList(pageNum, pageSize, null,sort);
        String newUrl = "/"+kind+"/new/page/1";
        String hotUrl = "/"+kind+"/hot/page/1";
        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/");
        pageUrl.append(kind);
        pageUrl.append("/");
        pageUrl.append(sort);
        pageUrl.append("/page/");
        model.addAttribute("pageUrl",pageUrl);

        if ("all".equals(kind)){
            model.addAttribute("condition",null);
            pageInfo = questionService.questionList(pageNum, pageSize, null,sort);
        }

        if ("search".equals(kind)){
            String keywordTrim = keyword.trim();
            String  condition = "?keyword="+keywordTrim;
            model.addAttribute("condition",condition);
            newUrl= newUrl+condition;
            hotUrl = hotUrl + condition;
            pageInfo = questionService.findQuestionByKeyword(pageNum, pageSize, keywordTrim,sort);
        }
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("newUrl",newUrl);
        model.addAttribute("hotUrl",hotUrl);
        model.addAttribute("sort",sort);
        model.addAttribute("type",null);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("hotTags",hotTags);
        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        model.addAttribute("commentCountUsers",commentCountUsers);

        return "jie/index";
    }

    @GetMapping("/tag/{tag}/page/{pageNum}")
    public String index(@PathVariable("tag") String tag,
                        @PathVariable("pageNum") Integer pageNum,
                        Model model){
        PageInfo<QuestionDTO> pageInfo = questionService.findQuestionByTag(tag,pageNum,pageSize);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("tag",tag);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("hotTags",hotTags);
        model.addAttribute("type",null);
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = hotUserCache.getUserCache();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = hotUserCache.getHotQuestionCache();
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/tag/");
        pageUrl.append(tag);
        pageUrl.append("/page/");
        model.addAttribute("pageUrl",pageUrl);
        return "jie/index";
    }

}
