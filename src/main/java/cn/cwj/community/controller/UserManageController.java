package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.TableDTO;

import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.model.Role;
import cn.cwj.community.model.SystemUser;
import cn.cwj.community.model.User;
import cn.cwj.community.service.SystemRoleService;
import cn.cwj.community.service.SystemUserService;
import cn.cwj.community.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/3/7
 * @Version V1.0
 **/
@RequestMapping("/system")
@Controller
public class UserManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 查询所用网站用户
     * @param siteUser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/siteUser")
    @ResponseBody
    public TableDTO siteUserSelect(User siteUser,
                               @RequestParam(name = "page",defaultValue = "1") Integer pageNum,
                               @RequestParam(name = "limit",defaultValue = "8") Integer pageSize
                               ){
        TableDTO allSiteUser = userService.findAllSiteUser(pageNum, pageSize, siteUser);
        return allSiteUser;

    }

    /**
     * 查询系统用户
     * @param systemUser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/systemUser")
    @ResponseBody
    public TableDTO systemUserSelect(SystemUser systemUser,
                                     @RequestParam(name = "page",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "limit",defaultValue = "10") Integer pageSize
    ){
        TableDTO allSystemUser = systemUserService.findAllSystemUser(pageNum,pageSize,systemUser);
        return allSystemUser;

    }
    @RequestMapping("/systemRole")
    @ResponseBody
    public TableDTO systemUserRoleSelect(@RequestParam(name = "page",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "limit",defaultValue = "10") Integer pageSize
    ){
        TableDTO allSystemRole = systemRoleService.findAllSystemRoel(pageNum,pageSize);
        return allSystemRole;

    }
    /**
     * 修改用户之用户信息回显
     * @param editType
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{opType}/{editType}/{id}")
    public String editUser(@PathVariable String editType,
                           @PathVariable String opType,
                           @PathVariable Long id,
                           Model model){
        String page = "";
        Object user = null;
        List<Role> roles = null;
        if ("siteUser".equals(editType) && "edit".equals(opType)){
            user = userService.findById(id);
            page = "system/edit";
        }
        if ("systemUser".equals(editType) && "edit".equals(opType)){
            user = systemUserService.findSystemUserById(id);

            List<Role> allRole = systemRoleService.selectAllRole();
            roles = allRole;
            page = "system/edit_system_user";
        }

        if ("systemRole".equals(editType) && "edit".equals(opType)){
            user = systemRoleService.findById(id);
            page = "system/edit_system_role";
        }

        if ("siteUser".equals(editType)&&"add".equals(opType)){
            page = "system/edit";
        }
        if ("systemUser".equals(editType)&&"add".equals(opType)){
            page = "system/edit_system_user";
            List<Role> allRole = systemRoleService.selectAllRole();
            roles = allRole;
        }
        if ("systemRole".equals(editType)&&"add".equals(opType)){
            page = "system/edit_system_role";
        }
        model.addAttribute("roles",roles);
        model.addAttribute("editUser",user);
        return page;
    }

    /**
     * 操作网站用户
     * @param user
     * @return
     */
    @PostMapping("/{type}/siteUser")
    @ResponseBody
    public ResultDTO editUser(User user,
                              @PathVariable String type){
        if ("edit".equals(type)){
            userService.insertOrEdit(user);
            return ResultDTO.okOf(CommonEnum.SET_SUCCESS);
        }
        if ("add".equals(type)){

            userService.insertOrEdit(user);
            return ResultDTO.okOf(CommonEnum.ADD_SUCCESS);
        }
        if ("del".equals(type)){
            userService.deleteSiteUserById(user.getId());
            return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
        }
        if ("ban".equals(type)){
            userService.banUser(user);
            if (user.getStatus() == 1){
                return ResultDTO.okOf(CommonEnum.BAN_SUCCESS);
            }else {
                return ResultDTO.okOf(CommonEnum.NOBAN_SUCCESS);
            }
        }
        return ResultDTO.errorOf(CustomizeErrorCode.OPERATION_ERROR);
    }

    /**
     * 操作系统用户
     * @param systemUser
     * @param type
     * @return
     */
    @PostMapping("/{type}/systemUser")
    @ResponseBody
    public ResultDTO opSiteUser(SystemUser systemUser,
                                 @PathVariable String type){

        System.out.println("前端传过来的"+systemUser);

        if ("edit".equals(type)){
            systemUserService.editSystemUser(systemUser);
            return ResultDTO.okOf(CommonEnum.SET_SUCCESS);
        }
        if ("add".equals(type)){
            systemUserService.insertSystemUser(systemUser);
            return ResultDTO.okOf(CommonEnum.ADD_SUCCESS);
        }
        if ("del".equals(type)){
            systemUserService.deleteSystemUserById(systemUser.getId());
            return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.OPERATION_ERROR);

    }

    @PostMapping("/{type}/systemRole")
    @ResponseBody
    public ResultDTO opRole(Role role,
                                @PathVariable String type){
        if ("edit".equals(type)){
            systemRoleService.editSystemRole(role);
            return ResultDTO.okOf(CommonEnum.SET_SUCCESS);
        }
        if ("add".equals(type)){
            systemRoleService.insertSystemRole(role);
            return ResultDTO.okOf(CommonEnum.ADD_SUCCESS);
        }
        if ("del".equals(type)){
            systemRoleService.deleteSystemRoleById(role.getId());
            return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.OPERATION_ERROR);

    }
    /**
     * 批量删除用户
     * @param ids
     * @param userType
     * @return
     */
    @PostMapping("/del/all/{userType}")
    @ResponseBody
    public ResultDTO delSiteUsers(@RequestParam String ids,
                                  @PathVariable String userType){
        ArrayList arrayList = JSON.parseObject(ids, ArrayList.class);
        if ("siteUsers".equals(userType)){
            userService.deleteSiteUserMany(arrayList);
        }
        if ("systemUsers".equals(userType)){
            systemUserService.deleteSiteUserMany(arrayList);
        }
        if ("systemRoles".equals(userType)){
            systemRoleService.deleteRoleMany(arrayList);
        }
        return ResultDTO.okOf(CommonEnum.DELETE_SUCCESS);
    }



}
