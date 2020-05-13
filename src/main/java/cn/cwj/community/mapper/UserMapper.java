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
    @Select("SELECT u.`del`,u.`name`,u.`id`,u.`avatar_url`,COUNT(c.`id`) comment_counts FROM (SELECT * FROM community.`user` WHERE del = 0) u LEFT JOIN community.comment c ON u.`id` = c.`commentator`GROUP BY u.id ORDER BY comment_counts DESC LIMIT 20")
    List<UserDTO> selectByCommentCount();
}
