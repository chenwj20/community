package cn.cwj.community.service;

import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.mapper.SystemRoleMapper;
import cn.cwj.community.mapper.SystemuserRoleMapper;
import cn.cwj.community.model.Role;
import cn.cwj.community.model.SystemuserRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/3/6
 * @Version V1.0
 **/
@Service
public class SystemRoleService {
    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemuserRoleMapper systemuserRoleMapper;

    public List<Role> selectRoleByUser(Long uid) {
        Example systemuserRole = new Example(SystemuserRole.class);
        systemuserRole.createCriteria()
                .andEqualTo("userId",uid);
        List<SystemuserRole> systemuserRoles = systemuserRoleMapper.selectByExample(systemuserRole);

        List<Role> roles = selectRoleByZj(systemuserRoles);
        return roles;
    }

    public List<Role> selectRoleByZj(List<SystemuserRole> systemuserRoles) {
        List<Integer> ids = new ArrayList<>();
        for (SystemuserRole systemuserRole : systemuserRoles) {
            ids.add(systemuserRole.getRoleId());
        }
        Example example = new Example(Role.class);
        example.createCriteria()
                .andIn("id",ids);
        List<Role> roles = systemRoleMapper.selectByExample(example);
        return roles;
    }

    /**
     * 查询所有角色
     * @return
     */
    public List<Role> selectAllRole() {
        return systemRoleMapper.selectAll();
    }

    /**
     * 分页查询所有角色
     * @param pageNum
     * @param pageSize
     * @return
     */
    public TableDTO findAllSystemRoel(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Role> roles = systemRoleMapper.selectAll();
        PageInfo pageInfo = new PageInfo(roles);
        return TableDTO.okOf(roles,pageInfo.getTotal());
    }

    /**
     * 批量删除角色
     * @param arrayList
     */
    @Transactional
    public void deleteRoleMany(ArrayList arrayList) {

        //删除关联表的数据
        Example userRoleExample = new Example(SystemuserRole.class);
        userRoleExample.createCriteria()
                .andIn("roleId",arrayList);
        systemuserRoleMapper.deleteByExample(userRoleExample);

        Example example = new Example(Role.class);
        example.createCriteria()
                .andIn("id", arrayList);
        systemRoleMapper.deleteByExample(arrayList);
    }

    public void editSystemRole(Role role) {
        systemRoleMapper.updateByPrimaryKey(role);
    }

    public void insertSystemRole(Role role) {
        systemRoleMapper.insert(role);
    }

    /**
     * 删除角色，及其关联表中的数据
     * @param id
     */
    @Transactional
    public void deleteSystemRoleById(Integer id) {
        //删除关联表的数据
        Example userRoleExample = new Example(SystemuserRole.class);
        userRoleExample.createCriteria()
                .andEqualTo("roleId",id);
        systemuserRoleMapper.deleteByExample(userRoleExample);

        //删除角色表中的数据

        systemRoleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    public Object findById(Long id) {
        return systemRoleMapper.selectByPrimaryKey(id);
    }
}
