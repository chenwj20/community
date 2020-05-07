package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "question")

public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private String description;
    private Integer category;
    private Integer miCoin;
    private Integer isShow;//3封禁
    private Integer status;
    private Long acceptId;
    @Transient
    private String creatorName;

}