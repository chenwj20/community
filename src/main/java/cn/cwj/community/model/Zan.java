package cn.cwj.community.model;

import lombok.Data;

/**
 * @Date 2020/1/31
 * @Version V1.0
 **/
@Data
public class Zan {
    private Long id;
    private Integer type;
    private Long uid;
    private Long parentId;
}
