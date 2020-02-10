package cn.cwj.community.dto;

import cn.cwj.community.model.User;
import lombok.Data;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer commentCount;
    private String content;
    private User user;
    private boolean flag;
}
