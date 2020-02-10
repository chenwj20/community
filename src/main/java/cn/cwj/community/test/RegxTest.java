package cn.cwj.community.test;

import java.util.regex.Pattern;

/**
 * @Date 2020/2/8
 * @Version V1.0
 **/
public class RegxTest {
    public static void main(String[] args) {
        String content = "12we342efdsffdsdfsfsdfsfd";
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        boolean matches = Pattern.matches(pattern, content);
        System.out.println(matches);
    }
}
