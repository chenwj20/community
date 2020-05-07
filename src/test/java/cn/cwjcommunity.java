package cn;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/
public class cwjcommunity {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        System.out.println(System.currentTimeMillis());
    }
}
