package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Date 2020/1/18
 * @Version V1.0
 * 收藏
 **/
@Data
@Table(name = "collect")
public class Collect {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long questionId;
    private Long userId;
    private Long gmtCreate;
}
