package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.Table;

/**
 * @Date 2020/3/6
 * @Version V1.0
 **/
@Table(name = "systemuser_role")
@Data
public class SystemuserRole {
    private Integer userId;
    private Integer roleId;

    public SystemuserRole() {
    }

    public SystemuserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
