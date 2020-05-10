package cn.cwj.community.service;

import cn.cwj.community.dto.SystemUserDTO;
import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.mapper.SystemUserMapper;
import cn.cwj.community.mapper.SystemuserRoleMapper;
import cn.cwj.community.model.Role;
import cn.cwj.community.model.SystemUser;
import cn.cwj.community.model.SystemuserRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/3/3
 * @Version V1.0
 **/
@Service
public class SystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemuserRoleMapper systemuserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //通过用户名查询
    public SystemUser findAdminUserByUserName(String username) {
        Example example = new Example(SystemUser.class);
        example.createCriteria()
                .andEqualTo("username",username);
        SystemUser systemUser = systemUserMapper.selectOneByExample(example);
        return systemUser;
    }

    public TableDTO findAllSystemUser(Integer pageNum, Integer pageSize, SystemUser systemUser) {
        if ("".equals(systemUser.getEmail())){
            systemUser.setEmail(null);
        }
        if ("".equals(systemUser.getUsername())){
            systemUser.setUsername(null);
        }

        PageHelper.startPage(pageNum,pageSize);
        List<SystemUser> systemUsers = systemUserMapper.select(systemUser);
        PageInfo pageInfo = new PageInfo(systemUsers);
        List<SystemUserDTO> systemUserDTOs = new ArrayList<>();
        for (SystemUser systemUser1 : systemUsers) {
            SystemUserDTO systemUserDTO = new SystemUserDTO();
            List<Role> roles = systemRoleService.selectRoleByUser(systemUser1.getId());
            StringBuilder sb = new StringBuilder();
            for (Role role : roles) {
                sb.append(role.getZhName());
                sb.append(" ");
            }
            BeanUtils.copyProperties(systemUser1,systemUserDTO);
            systemUserDTO.setRoles(sb.toString());
            systemUserDTOs.add(systemUserDTO);
        }
        return TableDTO.okOf(systemUserDTOs,pageInfo.getTotal());

    }

    /**
     * 通过id删除用户
     * @param id
     */
    @Transactional
    public void deleteSystemUserById(Long id) {
        Example example = new Example(SystemuserRole.class);
        example.createCriteria()
                .andEqualTo("userId",id);
        systemuserRoleMapper.deleteByExample(example);

        systemUserMapper.deleteByPrimaryKey(id);
    }

    /**
     * 添加系统用户
     * @param systemUser
     */
    @Transactional
    public void insertSystemUser(SystemUser systemUser) {
        //把角色字符串1转换为数组

        systemUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));

        String encode = passwordEncoder.encode(systemUser.getPassword());
        System.out.println("密码为："+encode);


        systemUserMapper.insertSelective(systemUser);
        String[] roleIds = systemUser.getRoleIds().split(",");
        for (String roleId : roleIds) {
            //添加角色关联
            SystemuserRole systemuserRole = new SystemuserRole();
            systemuserRole.setRoleId(Integer.parseInt(roleId));
            systemuserRole.setUserId(systemUser.getId().intValue());
            systemuserRoleMapper.insert(systemuserRole);
        }
    }

    public void editSystemUser(SystemUser systemUser) {
        systemUserMapper.updateByPrimaryKey(systemUser);
    }

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    public SystemUser findSystemUserById(Long id) {
        List<Role> roles = systemRoleService.selectRoleByUser(id);
        SystemUser systemUser = systemUserMapper.selectByPrimaryKey(id);
        systemUser.setRoles(roles);
        return systemUser;
    }

    /**
     * 批量删除系统用户
     * @param arrayList
     */
    @Transactional
    public void deleteSystemUserMany(ArrayList arrayList) {
        Example example = new Example(SystemuserRole.class);
        example.createCriteria()
                .andIn("userId",arrayList);
        systemuserRoleMapper.deleteByExample(example);

        for ( Object i : arrayList) {
            systemUserMapper.deleteByPrimaryKey((i));
        }
    }
}
