package cn.cwj.community.cache;

import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.schedule.HotUserTasks;
import cn.cwj.community.service.UserService;
import cn.cwj.community.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Date 2020/3/16
 * @Version V1.0
 **/
@Component
public class HotUserCache {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private HotUserTasks hotUserTasks;

    /**
     * 获得缓存中的User
     * @return
     */
    public List getUserCache(){
        String commentCountUsers = redisUtil.get("commentCountUsers");
        if (StringUtils.isBlank(commentCountUsers)){
            List<UserDTO> listCommentCountUser = userService.findByCommentCount();
            System.out.println("没有缓存");
            redisUtil.set("commentCountUsers",JSON.toJSONString(listCommentCountUser));
            return listCommentCountUser;
        }
        List list = JSON.parseObject(commentCountUsers, ArrayList.class);
        return list;
    }

    public List getHotQuestionCache(){
        Set<String> commentCountQuestions = redisUtil.revRangeZSet("commentCountQuestions", 0, -1);
        if (CollectionUtils.isEmpty(commentCountQuestions)){
            hotUserTasks.hotQuestionSchedule();
            commentCountQuestions = redisUtil.revRangeZSet("commentCountQuestions", 0, -1);
        }
        List list = new ArrayList<>();
        for (String commentCountQuestion : commentCountQuestions) {
            list.add(JSON.parse(commentCountQuestion));
        }
        return list;
    }
}
