package cn.cwj.community.service;

import cn.cwj.community.dto.TagDTO;
import cn.cwj.community.mapper.UserTagMapper;
import cn.cwj.community.model.User;
import cn.cwj.community.model.UserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/2/5
 * @Version V1.0
 **/
@Service
public class UserTagService {
    @Autowired
    private UserTagMapper userTagMapper;
    public void addTag(Long uid, UserTag userTag) {
        userTag.setUid(uid);
        userTagMapper.insertSelective(userTag);
    }

    /**
     * 检查标签是否存在
     * @param tag
     * @param uid
     * @return
     */
    public boolean checkUserTag(UserTag tag, Long uid) {
        Example example = new Example(UserTag.class);
        example.createCriteria()
                .andEqualTo("uid",uid)
                .andEqualTo("tag",tag.getTag());
        List<UserTag> userTags = userTagMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userTags)){
            return false;
        }
        return true;
    }

    /**
     * 查询用户创建的标签
     * @param uid
     * @return
     */
    public List<String> findUserTag(Long uid) {
        Example example = new Example(UserTag.class);
        example.createCriteria().andEqualTo("uid",uid);
        List<UserTag> userTags = userTagMapper.selectByExample(example);
        List<String> tags = new ArrayList<>();
        tags.add("test");
        if (CollectionUtils.isEmpty(userTags)){
            return tags;
        }

        for (UserTag userTag : userTags) {

            tags.add(userTag.getTag());
        }
        return tags;
    }
}
