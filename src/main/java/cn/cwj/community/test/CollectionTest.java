package cn.cwj.community.test;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/2/3
 * @Version V1.0
 **/
public class CollectionTest {
    public static void main(String[] args) {
        List list = null;
        boolean empty = CollectionUtils.isEmpty(list);
        System.out.println(empty);
    }
}
