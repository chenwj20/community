package cn.cwj.community.dto;

import lombok.Data;

/**
 * @Date 2020/2/13
 * @Version V1.0
 **/
@Data
public class QQAccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String grant_type;
}
