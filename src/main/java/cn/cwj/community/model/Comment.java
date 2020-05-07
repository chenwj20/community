package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@Data
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private Integer type;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer commentCount;
    private Long commentator;
    private String parentTitle;
    @Transient
    private String commentatorName;
}
