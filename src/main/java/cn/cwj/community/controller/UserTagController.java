package cn.cwj.community.controller;

import cn.cwj.community.cache.TagCache;
import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.User;
import cn.cwj.community.model.UserTag;
import cn.cwj.community.service.UserService;
import cn.cwj.community.service.UserTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2020/2/5
 * @Version V1.0
 **/
@Controller
@RestController
@RequestMapping("/userTag")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public ResultDTO addTag(HttpServletRequest request,
                            UserTag tag
                            ){
        User user = (User)request.getSession().getAttribute("user");
         if (user == null){
             return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
         }
         if (StringUtils.isBlank(tag.getTag())){
             return ResultDTO.errorOf(CustomizeErrorCode.TAG_NOT_BLANK);
         }
         if (StringUtils.isBlank(tag.getBio())){
             return ResultDTO.errorOf(CustomizeErrorCode.TAGBIO_NOT_BLANK);
         }
         if (StringUtils.isBlank(tag.getTag())){
             return ResultDTO.okOf(CommonEnum.LV_LOWER);
         }
         User lvUser = userService.findLvById(user.getId());
         if (lvUser.getLv()<3){
             return ResultDTO.okOf(CommonEnum.LV_LOWER);
         }
         List<String> tagList = TagCache.getTagList();
         boolean b = userTagService.checkUserTag(tag,user.getId());
         if (tagList.contains(tag.getTag()) || b){
             return ResultDTO.errorOf(CustomizeErrorCode.TAG_IS_EXIST);
         }
        userTagService.addTag(user.getId(),tag);
         return ResultDTO.okOf(CommonEnum.TAG_ADD__SUCCESS);
    }
}
