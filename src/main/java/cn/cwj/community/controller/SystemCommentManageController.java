package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.model.Comment;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.User;
import cn.cwj.community.service.CommentService;
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
@RequestMapping("/system/comment")
@Controller
public class SystemCommentManageController {
    @Autowired
    private CommentService commentService;
    @RequestMapping("/all")
    @ResponseBody
    public TableDTO questionSelect(Comment comment,
                                   @RequestParam(name = "page",defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "limit",defaultValue = "8") Integer pageSize)
    {
        TableDTO allComment = commentService.findCommentBySome(pageNum,pageSize,comment);
        return allComment;

    }

    /**
     * 后台操作帖子
     * @param type
     * @param id
     * @return
     */
    @RequestMapping("/{type}/{id}/{parentId}")
    @ResponseBody
    public ResultDTO banQuestion(@PathVariable String type,@PathVariable Long id,
                                 @PathVariable Long parentId){
        ResultDTO resultDTO = new ResultDTO();

        if ("del".equals(type)){
            commentService.removeComment(id,parentId,null);
            resultDTO = ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
        }
        return resultDTO;
    }

    @PostMapping("/del/selected")
    @ResponseBody
    public ResultDTO delQuestions(@RequestParam String ids){
        ArrayList arrayList = JSON.parseObject(ids, ArrayList.class);
        commentService.deleteCommentSelected(arrayList);
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }
}
