package cn.cwj.community.service;

import cn.cwj.community.mapper.CommentMapper;
import cn.cwj.community.mapper.QuestionMapper;
import cn.cwj.community.mapper.ZanMapper;
import cn.cwj.community.model.Comment;
import cn.cwj.community.model.Question;
import cn.cwj.community.model.Zan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Date 2020/1/31
 * @Version V1.0
 **/
@Service
public class ZanService {

    @Autowired
    private ZanMapper zanMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 取消点赞
     * @param id
     * @param uid
     * @param type
     */
    @Transactional
    public void removeZan(Long id, Long uid, Integer type) {
        if (type == 1){
            Question question = questionMapper.selectByPrimaryKey(id);
            question.setLikeCount(question.getLikeCount()-1);
            questionMapper.updateByPrimaryKeySelective(question);
        }else {
            Comment comment = commentMapper.selectByPrimaryKey(id);
            comment.setLikeCount(comment.getLikeCount()-1);
            commentMapper.updateByPrimaryKeySelective(comment);
        }
        Example example = new Example(Zan.class);
        example.createCriteria()
                .andEqualTo("uid",uid)
                .andEqualTo("parentId",id)
                .andEqualTo("type",type);
        zanMapper.deleteByExample(example);
    }

    /**
     * 点赞
     * @param id
     * @param userId
     */
    @Transactional
    public void addZan(Long id, Long userId,Integer type) {

        if (type == 1){
            //点赞帖子
            Question question = questionMapper.selectByPrimaryKey(id);
            question.setLikeCount(question.getLikeCount()+1);
            questionMapper.updateByPrimaryKeySelective(question);
        }else {
            Comment comment = commentMapper.selectByPrimaryKey(id);
            comment.setLikeCount(comment.getLikeCount()+1);
            commentMapper.updateByPrimaryKeySelective(comment);
        }
        Zan zan = new Zan();
        zan.setParentId(id);
        zan.setType(type);
        zan.setUid(userId);
        zanMapper.insertSelective(zan);
    }

    /**
     * 判断是否点赞
     * @param id
     * @param uid
     * @param type
     * @return
     */
    public boolean checkZan(Long id, Long uid,Integer type) {
        Example example = new Example(Zan.class);
        example.createCriteria()
                .andEqualTo("uid",uid)
                .andEqualTo("parentId",id)
                .andEqualTo("type",type);
        List<Zan> zans = zanMapper.selectByExample(example);
        return !CollectionUtils.isEmpty(zans);
    }
}
