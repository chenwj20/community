package cn.cwj.community.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/2/3
 * @Version V1.0
 **/
public class CollectionTest {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        CollectionTest collectionTest = new CollectionTest();
        String qqq = collectionTest.qqq("123456");
        System.out.println(qqq);
    }
    public  String qqq(String password) {
        String encode = new BCryptPasswordEncoder().encode(password);
        return encode;
    }
}
