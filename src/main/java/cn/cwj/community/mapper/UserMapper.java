package cn.cwj.community.mapper;

import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Date 2020/1/12
 * @Version V1.0
 **/
@Repository
public interface UserMapper extends Mapper<User> {
    @Select("SELECT user.`name`,user.`id`,user.`avatar_url`,COUNT(comment.`id`) comment_counts FROM USER LEFT JOIN COMMENT ON user.`id` = comment.`commentator`GROUP BY comment.`commentator` ORDER BY comment_counts DESC LIMIT 20")
    List<UserDTO> selectByCommentCount();
}
