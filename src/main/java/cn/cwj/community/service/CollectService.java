package cn.cwj.community.service;

import cn.cwj.community.mapper.CollectMapper;
import cn.cwj.community.model.Collect;
import cn.cwj.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Date 2020/1/18
 * @Version V1.0
 **/
@Service
public class CollectService {
    @Autowired
    private CollectMapper collectMapper;

    /**
     * 收藏功能
     * @param questionId
     * @param user
     */
    public void insertCollect(Long questionId, User user){
        Collect collect = new Collect();
        collect.setQuestionId(questionId);
        collect.setUserId(user.getId());
        collect.setGmtCreate(System.currentTimeMillis());
        collectMapper.insert(collect);
    }

    /**
     * 取消收藏
     * @param questionId
     * @param user
     */
    public void deleteCollect(Long questionId, User user) {
        Collect collect = new Collect();
        collect.setUserId(user.getId());
        collect.setQuestionId(questionId);
        collectMapper.delete(collect);
    }

    /**
     * 查询用户收藏帖子
     * @param uid
     */
    public String findByUserId(Long uid,Long questionId){
        Example example = new Example(Collect.class);
        example.createCriteria().andEqualTo("userId",uid);
        List<Collect> collects = collectMapper.selectByExample(example);
        String flag = "0";
        for (Collect collect : collects) {
            if (collect.getQuestionId() == questionId){
                flag = "1";
                break;
            }
        }
        return flag;
    }
}
