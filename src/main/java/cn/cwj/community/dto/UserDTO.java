package cn.cwj.community.dto;

import cn.cwj.community.model.Comment;
import cn.cwj.community.model.Question;
import cn.cwj.community.service.CommentService;
import lombok.Data;

import java.util.List;

/**
 * @Date 2020/1/18
 * @Version V1.0
 **/
@Data
public class UserDTO {
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
    private Integer CommentCounts;
    private Integer miCoin;
    private Integer experience;
    private Integer lv;
    private Integer gender;
    private List<Question> questions;
    private Integer role;
    private List<Comment> comments;
}
