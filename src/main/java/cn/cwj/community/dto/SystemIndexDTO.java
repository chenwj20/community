package cn.cwj.community.dto;

import cn.cwj.community.model.User;
import lombok.Data;

import java.util.List;

/**
 * @Date 2020/3/25
 * @Version V1.0
 **/
@Data
public class SystemIndexDTO {
    private int userCount;
    private int questionCount;
    private int commentCount;
    private List<User> users;
}
