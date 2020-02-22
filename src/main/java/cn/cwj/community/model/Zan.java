package cn.cwj.community.model;

import lombok.Data;

/**
 * @Date 2020/1/31
 * @Version V1.0
 **/
@Data
public class Zan {
    private Long id;
    private Integer type;//1为问题，2为评论
    private Long uid;
    private Long parentId;
}
