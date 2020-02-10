package cn.cwj.community.test;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @Date 2020/2/2
 * @Version V1.0
 **/
public class MD5Test {
    public static void main(String[] args) {
        String s = "123456";
        String strh = s.substring(s.length() -2,s.length());
        System.out.println(strh);
        String md5Hex1 = DigestUtil.md5Hex("1234");
        System.out.println(md5Hex1);
    }
}
