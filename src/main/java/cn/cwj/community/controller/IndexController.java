package cn.cwj.community.controller;

import cn.cwj.community.cache.HotTagCache;
import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.model.Question;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class IndexController {
    private Integer pageSize = 16;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;
    @GetMapping("/")
    public String index(Model model){
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = userService.findByCommentCount();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = questionService.findByComments(1,10);
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        PageInfo<QuestionDTO> pageInfo = questionService.questionList(1, 20, null,null);
        List<QuestionDTO> topList = questionService.findByStatus(1);
        model.addAttribute("topList",topList);
        model.addAttribute("newUrl","/all/new/page/"+1);
        model.addAttribute("hotUrl","/all/hot/page/"+1);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("hotTags",hotTags);

        model.addAttribute("type",null);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
       }

    @GetMapping("/all/page/{pageNum}")
    public String index(Model model,
                        @PathVariable("pageNum") Integer pageNum){
        PageInfo<QuestionDTO> pageInfo = questionService.questionList(pageNum, pageSize, null,null);
        List<String> hotTags = hotTagCache.getHots();
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = userService.findByCommentCount();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = questionService.findByComments(1,10);
        model.addAttribute("hotTags",hotTags);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("newUrl","/all/new/page/"+pageNum);
        model.addAttribute("hotUrl","/all/hot/page/"+pageNum);
        model.addAttribute("type",null);
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        model.addAttribute("pageUrl","/all/page/");
        return "jie/index";
    }


    @GetMapping("/all/{sort}/page/{pageNum}")
    public String index(Model model,
                        @PathVariable("pageNum") Integer pageNum,
                        @PathVariable("sort") String sort){
        PageInfo<QuestionDTO> pageInfo = questionService.questionList(pageNum, pageSize, null,sort);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("newUrl","/all/new/page/"+pageNum);
        model.addAttribute("hotUrl","/all/hot/page/"+pageNum);
        model.addAttribute("sort",sort);
        model.addAttribute("type",null);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("hotTags",hotTags);
        List<Question> commentCountQuestions = questionService.findByComments(1,10);
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = userService.findByCommentCount();
        model.addAttribute("commentCountUsers",commentCountUsers);
        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/all/");
        pageUrl.append(sort);
        pageUrl.append("/page/");
        model.addAttribute("pageUrl",pageUrl);
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
        //查询评论最多用户信息
        List<UserDTO> commentCountUsers = userService.findByCommentCount();
        model.addAttribute("commentCountUsers",commentCountUsers);
        List<Question> commentCountQuestions = questionService.findByComments(1,10);
        model.addAttribute("commentCountQuestions",commentCountQuestions);
        StringBuffer pageUrl = new StringBuffer();
        pageUrl.append("/tag/");
        pageUrl.append(tag);
        pageUrl.append("/page/");
        model.addAttribute("pageUrl",pageUrl);
        return "jie/index";
    }
}
