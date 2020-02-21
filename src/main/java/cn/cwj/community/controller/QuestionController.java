package cn.cwj.community.controller;

import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommentTypeEnum;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.exception.CustomizeException;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.User;
import cn.cwj.community.service.CollectService;
import cn.cwj.community.service.CommentService;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.ZanService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2020/1/14
 * @Version V1.0
 **/
@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    CollectService collectService;
    @Autowired
    ZanService zanService;
    @GetMapping("/remove/{id}")
    @ResponseBody
    public ResultDTO removeQuestion(@PathVariable("id") Long id,
                                    HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        questionService.removeQuestion(id,user);
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }

    /**
     * 彻底删除
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResultDTO deleteQuestion(@PathVariable("id") Long id,
                                 HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        questionService.deleteQuestion(id,user);
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }
    @GetMapping("/recycle/{id}")
    @ResponseBody
    public ResultDTO recycleQuestion(@PathVariable("id") Long id,
                                    HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        questionService.recycleQuestion(id,user);
        return ResultDTO.okOf(CommonEnum.RECYCLE_SUCCESS);
    }

    @GetMapping("/{id}/page/{pageNum}")
    public String question(@PathVariable("id") Long id,
                           @PathVariable("pageNum") Integer pageNum,
                           @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                           Model model,HttpServletRequest request){

        QuestionDTO questionDTO = questionService.findQuestionById(id);
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO);
        User user = (User)request.getSession().getAttribute("user");
        String flag ="0";
        boolean b = false;
        if (user != null){
            flag = collectService.findByUserId(user.getId(), id);
            b = zanService.checkZan(id,user.getId(),1);

        }
        PageInfo commentPageInfo = commentService.findByParentId(id, CommentTypeEnum.COMMENT_ONE.getType(), pageNum, pageSize,user);
        List <User> zanUsers = questionService.findZanUser(id);

        model.addAttribute("zanUsers",zanUsers);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("commentDTOS",commentPageInfo);
        model.addAttribute("relatedQuestions",relatedQuestions);
        model.addAttribute("collect",flag);
        model.addAttribute("isQuestionZan",b);
        return "jie/detail";
    }

    /**
     * 更新或删除所有问题
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/{type}")
    @ResponseBody
    public ResultDTO dORRAllQuestion(HttpServletRequest request,
                                           @PathVariable String type){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Integer type1 = 0;
        ResultDTO resultDTO = ResultDTO.okOf(CommonEnum.RECYCLE_SUCCESS);
        if (type.equals("delete")){
            resultDTO = ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
            type1 = 1;
        }
        questionService.recycleOrDeleteAllQuestion(user,type1);
        return resultDTO;
    }
    @GetMapping("/recycle/page/{pageNum}")
    public String recycle(HttpServletRequest request,
                          Model model,
                          @PathVariable("pageNum") Integer pageNum,
                          @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        PageInfo recyclePageInfo = questionService.findRecycle(user.getId(), pageNum, pageSize);
        model.addAttribute("recyclePageInfo",recyclePageInfo);
        return "user/recycle";
    }

    /**
     * 置顶问题
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/{top}/{id}")
    @ResponseBody
    public ResultDTO topQuestion(@PathVariable Long id,
                                 @PathVariable Integer top,
                                 HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        //1为置顶
        questionService.topQuestion(id,top);
        return ResultDTO.okOf(CommonEnum.Top_question__SUCCESS);
    }

    @RequestMapping("/{qid}/accept/{cid}")
    @ResponseBody
    public ResultDTO acceptComment(@PathVariable Long qid,
                                   @PathVariable Long cid,
                                 HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        questionService.acceptComment(qid,cid);
        return ResultDTO.okOf(CommonEnum.ACCEPT__SUCCESS);
    }
}
