package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.User;
import cn.cwj.community.service.QuestionService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @Date 2020/2/20
 * @Version V1.0
 **/
@RequestMapping("/system/question")
@Controller
public class SystemQuestionManageController {
    @Autowired
    private QuestionService questionService;
    @RequestMapping("/all")
    @ResponseBody
    public TableDTO questionSelect(Question question,
                                   @RequestParam(name = "page",defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "limit",defaultValue = "8") Integer pageSize)
    {
        TableDTO allQuestion = questionService.findQuestionBySome(pageNum,pageSize,question);
        return allQuestion;

    }

    /**
     * 后台操作帖子
     * @param type
     * @param id
     * @return
     */
    @RequestMapping("/{type}/{id}")
    @ResponseBody
    public ResultDTO banQuestion(@PathVariable String type,@PathVariable Long id){
        ResultDTO resultDTO = new ResultDTO();
        if ("ban".equals(type)){
            //3，代表封禁帖子
            questionService.banOrNoQuestion(id,3);
            resultDTO = ResultDTO.okOf(CommonEnum.BAN_SUCCESS);
        }
        if ("noBan".equals(type)){
            //1，代表显示帖子
            questionService.banOrNoQuestion(id,1);
            resultDTO = ResultDTO.okOf(CommonEnum.NOBAN_SUCCESS);
        }
        if ("del".equals(type)){
            User user = new User();
            user.setId(-999l);
            questionService.deleteQuestion(id,user);
            resultDTO = ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
        }
        if ("fine".equals(type)){
            questionService.fineQuestion(id,1);
            resultDTO = ResultDTO.okOf(CommonEnum.FINE_SUCCESS);
        }
        if ("noFine".equals(type)){
            questionService.fineQuestion(id,0);
            resultDTO = ResultDTO.okOf(CommonEnum.CANCEL_SUCCESS);
        }
        return resultDTO;
    }

    @PostMapping("/del/selected")
    @ResponseBody
    public ResultDTO delQuestions(@RequestParam String ids){
        ArrayList arrayList = JSON.parseObject(ids, ArrayList.class);
        questionService.deleteQuestionSelected(arrayList);
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }
}
