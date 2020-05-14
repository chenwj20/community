package cn.cwj.community.schedule;

import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.model.Question;
import cn.cwj.community.service.QuestionService;
import cn.cwj.community.service.UserService;
import cn.cwj.community.util.RedisUtil;
import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Date 2020/2/16
 * @Version V1.0
 **/
@Component
public class HotUserTasks {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    @Scheduled(cron="0 15 0 * * ?")//每天凌晨00:15触发
    public void hotUserSchedule(){
        List<UserDTO> userDTOS = userService.findByCommentCount();
        redisUtil.set("commentCountUsers", JSON.toJSONString(userDTOS));
    }

    @Scheduled(cron="0 16 0 * * ?")//每天凌晨00:16触发
    public void hotQuestionSchedule(){
        List<Question> questions = questionService.findByComments(1, 15);
        redisUtil.delete("commentCountQuestions");
        for (Question question : questions) {
            redisUtil.addZSet("commentCountQuestions",JSON.toJSONString(question),question.getCommentCount()*3+question.getViewCount());
        }
    }
}
