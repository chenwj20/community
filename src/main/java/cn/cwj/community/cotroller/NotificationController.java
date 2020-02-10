package cn.cwj.community.cotroller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.Notification;
import cn.cwj.community.model.User;
import cn.cwj.community.service.NotificationService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2020/1/16
 * @Version V1.0
 **/
@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all/page/{pageNum}")
    public String findAllNotification(HttpServletRequest request,
                                      Model model,
                                      @PathVariable("pageNum") Integer pageNum,
                                      @RequestParam(defaultValue = "8") Integer pageSize){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return  "user/login";
        }
        PageInfo<Notification> pageInfoNotifications = notificationService.findAllNotification(user.getId(), pageNum, pageSize);
        model.addAttribute("pageInfoNotifications",pageInfoNotifications);
        return "user/notification";
    }

    @RequestMapping("/nums")
    @ResponseBody
    public ResultDTO getNotificationCount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Integer count = notificationService.findNotificationCount(user.getId());
        return ResultDTO.okOf(count);
    }

    @RequestMapping("/remove")
    @ResponseBody
    public ResultDTO removeAllNotification(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        notificationService.removeAllNotification(user.getId());
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }

    @RequestMapping("/remove/{id}")
    @ResponseBody
    public ResultDTO removeOneNotification(@PathVariable("id")Long id){
        notificationService.removeOneNotification(id);
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }

    @RequestMapping("/status")
    @ResponseBody
    public ResultDTO setNotificationRead(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        notificationService.setNotificationStatus(user.getId());
        return ResultDTO.okOf();
    }

    @RequestMapping("/status/{id}")
    @ResponseBody
    public ResultDTO  setOneNotificationRead(HttpServletRequest request,
                                             @PathVariable("id") Long id){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        notificationService.setOneNotificationStatus(id,user.getId());
        return ResultDTO.okOf();
    }
}
