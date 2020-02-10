package cn.cwj.community.dto;

import lombok.Data;

/**
 * @Date 2020/2/4
 * @Version V1.0
 **/
@Data
public class SignInUserDTO {
    private Long uid;
    private String avatarUrl;
    private Long gmtCreate;
    private Integer continueSign;
    private Integer count;

}
