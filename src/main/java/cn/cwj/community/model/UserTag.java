package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Date 2020/2/5
 * @Version V1.0
 **/
@Data
@Table(name = "user_tag")
public class UserTag {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    private String tag;
    private String bio;
}
