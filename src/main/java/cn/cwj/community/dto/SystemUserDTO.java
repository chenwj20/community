package cn.cwj.community.dto;

import cn.cwj.community.model.Role;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @Date 2020/3/2
 * @Version V1.0
 **/
@Data
public class SystemUserDTO {
    private Long id;
    private String username;
    private Long gmtCreate;
    private String bio;
    private String email;
    private String phone;
    private String roles;
}
