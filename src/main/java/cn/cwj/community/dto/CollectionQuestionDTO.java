package cn.cwj.community.dto;

import lombok.Data;

/**
 * @Date 2020/1/30
 * @Version V1.0
 **/
@Data
public class CollectionQuestionDTO {
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
    private String category;
    private String experience;
    private Integer isShow;
    private Integer status;
    private Long collectionTime;
}
