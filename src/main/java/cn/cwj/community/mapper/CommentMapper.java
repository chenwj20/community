package cn.cwj.community.mapper;

import cn.cwj.community.model.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CommentMapper extends Mapper<Comment> {
    @Select("select count(*) comments from comment where commentator = #{id} order by comments desc limit 0,12 ")
    Integer selectCountByCommentator(@Param("id") Long id);
}
