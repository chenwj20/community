package cn.cwj.community.test;

import java.text.DecimalFormat;

/**
 * @Date 2020/2/6
 * @Version V1.0
 **/
public class LvTest {
    public static void main(String[] args) {
        float num = (float) 33/190;
        DecimalFormat df = new DecimalFormat("0.00%");
        String s = df.format(num);
        System.out.println(s);
    }
}
