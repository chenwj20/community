package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.User;
import cn.cwj.community.service.ZanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Date 2020/1/31
 * @Version V1.0
 **/
@RestController
@RequestMapping("/zan")
public class ZanController {
    @Autowired
    private ZanService zanService;
    @ResponseBody
    @RequestMapping("/add/{type}/{id}")
    public ResultDTO commentZan(@PathVariable Long id,
                                @PathVariable Integer type,
                                HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        zanService.addZan(id,user.getId(),type);
//        commentService.commentZan(id,user.getId());
        return ResultDTO.okOf(CommonEnum.ZAN_SUCCESS);
    }

    @RequestMapping("/remove/{type}/{id}")
    public ResultDTO removeZan(@PathVariable Long id,
                               @PathVariable Integer type,
                               HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        zanService.removeZan(id,user.getId(),type);
        return ResultDTO.okOf(CommonEnum.CANCEL_SUCCESS);
    }

}
