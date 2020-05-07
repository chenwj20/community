package cn.cwj.community;

import cn.cwj.community.cache.HotUserCache;
import cn.cwj.community.dto.CommentDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.schedule.HotUserTasks;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import cn.cwj.community.util.RedisUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.alibaba.fastjson.JSON;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;


/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonTest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private HotUserTasks hotUserTasks;
    @Autowired
    private HotUserCache hotUserCache;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Test
    public void test01(){
        long start = System.currentTimeMillis();
        String commentCountUsers = redisUtil.get("commentCountUsers");
        long end = System.currentTimeMillis();
        System.out.println(commentCountUsers);
        System.out.println("redis耗时："+(end-start));
        long start1 = System.currentTimeMillis();
        List<UserDTO> byCommentCount = userService.findByCommentCount();
        String string = JSON.toJSONString(byCommentCount);
        JSON.parseObject(string,List.class);
        long end1 = System.currentTimeMillis();
        System.out.println(byCommentCount);
        System.out.println("mysql耗时："+(end1-start1));

    }

}


