package cn.cwj.community.dto;


import lombok.Data;




/**
 * @Date 2020/3/7
 * @Version V1.0
 **/
@Data
public class UserTableDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    private String email;
    private String accountType;
    private Integer gender;
    private String bio;
    private Integer miCoin;
    private Integer lv;
    private Long gmtCreate;
    private Integer status;


}
