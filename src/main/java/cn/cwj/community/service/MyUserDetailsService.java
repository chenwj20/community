package cn.cwj.community.service;

import cn.cwj.community.mapper.SystemRoleMapper;
import cn.cwj.community.mapper.SystemuserRoleMapper;
import cn.cwj.community.model.SystemUser;
import cn.cwj.community.model.Role;
import cn.cwj.community.model.SystemuserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/3/6
 * @Version V1.0
 **/
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemuserRoleMapper systemuserRoleMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里可以可以通过username（登录时输入的用户名）然后到数据库中找到对应的用户信息，并构建成我们自己的UserInfo来返回。
        //这里可以通过数据库来查找到实际的用户信息，这里我们先模拟下,后续我们用数据库来实现
        SystemUser systemUser = systemUserService.findAdminUserByUserName(username);
        Example systemuserRole = new Example(SystemuserRole.class);
        systemuserRole.createCriteria()
                .andEqualTo("userId",systemUser.getId());
        List<SystemuserRole> systemuserRoles = systemuserRoleMapper.selectByExample(systemuserRole);

        List<Role> roles = systemRoleService.selectRoleByZj(systemuserRoles);
        User user = new User(systemUser.getUsername(), systemUser.getPassword(),getGrantedAuthority(roles));
        return user;
    }
    //返回查询列表
    public List<SimpleGrantedAuthority>  getGrantedAuthority(List<Role> list){
        List<SimpleGrantedAuthority>  grantedAuthorities = new ArrayList<>();
        for (Role role : list) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
        return grantedAuthorities;
    }
}
