package cn.cwj.community.controller;

import cn.cwj.community.cache.TagCache;
import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.TagDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.Category;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.User;
import cn.cwj.community.provider.TextAntispamScanSample;
import cn.cwj.community.service.CategoryService;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import cn.cwj.community.service.UserTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2020/1/12
 * @Version V1.0
 **/
@Controller
@Slf4j
public class PublishController {
    @Autowired
    QuestionService questionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserTagService userTagService;
    @GetMapping("publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "user/login";
        }
        QuestionDTO questionEdit = questionService.findQuestionById(id);
        model.addAttribute("questionEdit",questionEdit);
        List<String> list = userTagService.findUserTag(user.getId());
        List<TagDTO> tagDTOS = TagCache.tagList(list);
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("tags",tagDTOS);
        return "jie/publish";
    }
    @GetMapping("/publish")
    public String publish(HttpServletRequest request,Model model){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "user/login";
        }
        List<String> list = userTagService.findUserTag(user.getId());
        List<Category> categories = categoryService.selectCategory();
        model.addAttribute("categories",categories);
        model.addAttribute("questionEdit",null);
        List<TagDTO> tagDTOS = TagCache.tagList(list);
        model.addAttribute("tags",tagDTOS);
        return "jie/publish";
    }
    @PostMapping("/publish")
    @ResponseBody
    public ResultDTO doPublish(Question question,
                               HttpServletRequest request) throws Exception {
        User user = (User)request.getSession().getAttribute("user");
        if (StringUtils.isBlank(question.getTag())){
            return ResultDTO.errorOf(CustomizeErrorCode.TAG_ONE);
        }
        if (question.getId() == null && user.getMiCoin()<question.getMiCoin()){
            return ResultDTO.okOf(CommonEnum.MICOIN_LESS);
        }

        log.info("no tag {}" ,question.getTag());
        String tag = question.getTag().substring(1);
        log.info("sp tag {}" ,tag);
        question.setTag(tag);
        question.setCreator(user.getId());
        //内容检测
        boolean b = TextAntispamScanSample.checkText(question.getTitle() + question.getDescription());
        if (b){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_ILLEGAL);
        }
        questionService.createOrUpdateQuestion(question,user);
        return ResultDTO.okOf();
    }
}
