package cn.cwj.community.controller;

import cn.cwj.community.dto.CommentDTO;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommentTypeEnum;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.Comment;
import cn.cwj.community.model.User;
import cn.cwj.community.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @RequestMapping("/{id}")
    public  ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        PageInfo commentDTOPageInfo = commentService.findByParentId(id, CommentTypeEnum.COMMENT_TWO.getType(), 1, 10,null);
        return ResultDTO.okOf(commentDTOPageInfo);
    }
    @PostMapping("/add")
    public Object post(HttpServletRequest request,
                       Long toCommentId,
                          CommentDTO commentDTO){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        commentService.insertComment(comment,user,toCommentId);
        return ResultDTO.okOf(CommonEnum.COMMENT_SUCCESS);
    }
    @RequestMapping("/remove/{id}")
    public ResultDTO deleteComment(@PathVariable("id") Long id,
                                   @RequestParam("parentId") Long parentId,
                                   HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        commentService.removeComment(id,parentId,user.getId());
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);

    }
}
