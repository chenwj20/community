package cn.cwj.community.dto;

import lombok.Data;

/**
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Data
public class IsSignInDTO {
    //{days: 2, experience: 5, signed: true}
    private Integer days;
    private Integer experience;
    private boolean signed;
}
