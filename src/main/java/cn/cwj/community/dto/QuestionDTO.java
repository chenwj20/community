package cn.cwj.community.dto;

import cn.cwj.community.model.User;
import lombok.Data;

import java.util.List;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private String category;
    private Integer categoryI;
    private Integer miCoin;
    private Integer isShow;//是否展示
    private Integer status;//是否置顶，置顶为1
    private Long acceptId;//采纳的回答id
    private User user;
}
