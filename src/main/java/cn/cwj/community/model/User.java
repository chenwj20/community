package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String accountType;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
    private String bio;
    private String location;
    private String company;
    private String phone;
    private String password;
    private String email;
    private Integer miCoin;
    private Integer experience;
    private Integer lv;
    private Integer gender;//0为男，1为女
    private Integer role;
}