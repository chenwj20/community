package cn.cwj.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @Date 2020/3/7
 * @Version V1.0
 **/
@Data
public class TableDTO<T> {
    private Integer code;
    private String msg;
    private Long count;
    private T data;
    public static <T> TableDTO okOf(T t,Long count) {
        TableDTO tableDTO = new TableDTO();
        tableDTO.setCode(0);
        tableDTO.setMsg("请求成功");
        tableDTO.setCount(count);
        tableDTO.setData(t);
        return tableDTO;
    }
}
