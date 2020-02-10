package cn.cwj.community.cotroller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.User;
import cn.cwj.community.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Date 2020/1/18
 * @Version V1.0
 **/
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 添加收藏
     * @param questionId
     * @param request
     * @return
     */
    @GetMapping("/add")
    public ResultDTO add(Long questionId,
                         HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
//        ResultDTO resultDTO = new ResultDTO();
        if (user == null){
            ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        collectService.insertCollect(questionId,user);
        return ResultDTO.okOf();
    }
    @GetMapping("/remove")
    public ResultDTO remove(Long questionId,
                            HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        collectService.deleteCollect(questionId,user);
        return ResultDTO.okOf();
    }


}
