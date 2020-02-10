package cn.cwj.community.dto;

import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.exception.CustomizeException;
import lombok.Data;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@Data
public class ResultDTO<T> {
    private Integer code;
    private String msg;
    private T data;

    public static ResultDTO errorOf(Integer code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMsg(msg);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }
    public static ResultDTO okOf(CommonEnum commonEnum) {
        return okOf(commonEnum.getCode(), commonEnum.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMsg("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMsg("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO okOf(Integer code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMsg(msg);
        return resultDTO;
    }
}
