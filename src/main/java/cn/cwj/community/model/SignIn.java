package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Date 2020/1/29
 * @Version V1.0
 * 签到
 **/
@Data
@Table(name = "signin")
public class SignIn {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer continueSign;
    private Integer count;
}
