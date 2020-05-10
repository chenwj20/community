package cn.cwj.community.schedule;

import cn.cwj.community.cache.HotTagCache;
import cn.cwj.community.mapper.QuestionMapper;
import cn.cwj.community.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Date 2020/2/5
 * @Version V1.0
 **/
@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;
    //设置一天更新一次热门标签
    @Scheduled(fixedRate = 1000*60*60*24)
    public void hotTagSchedule(){
        int offset = 0;
        int limit = 20;
        List<Question> list = new ArrayList<>();

        Map<String,Integer> priorities = new HashMap<>();
        while (offset == 0 || list.size() == limit){
            list = questionMapper.selectByExampleAndRowBounds(new Example(Question.class), new RowBounds(offset, limit));
            for (Question question : list) {
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if (priority != null){
                        priorities.put(tag,priority+5+question.getCommentCount());
                    }else{
                        priorities.put(tag,5+question.getCommentCount());
                    }
                }
            }
            offset+= limit;
        }
        hotTagCache.updateTags(priorities);

    }
}
