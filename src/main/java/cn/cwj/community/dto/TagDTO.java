package cn.cwj.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @Date 2020/2/4
 * @Version V1.0
 **/
@Data
public class TagDTO {
    private String categoryName;
    private List<String> list;
}
