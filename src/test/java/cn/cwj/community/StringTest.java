package cn.cwj.community;

import org.junit.Test;

import java.util.Arrays;

/**
 * @Date 2020/1/30
 * @Version V1.0
 **/
public class StringTest {
    @Test
    public void test01(){
        String s = "@111";
        String[] arr = s.split("@");
        System.out.println(arr.length);
        String[] arr1 = arr[1].split(" ");
        for (String s1 : arr) {
            System.out.println("s1："+s1);
        }

        for (String s2 : arr1) {
            System.out.println("s2："+s2);
        }
        /*String name = s.split("@")[1].split(" ")[0];
        System.out.println(name);*/
    }
}
