package cn.cwj.community.dto;

import lombok.Data;

/**
 * @Date 2020/2/5
 * @Version V1.0
 **/
@Data
public class HotTagDTO implements Comparable{
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO)o).getPriority();
    }
}
