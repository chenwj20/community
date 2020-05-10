package cn.cwj.community.mapper;

import cn.cwj.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface QuestionMapper extends Mapper<Question> {
    @Select("SELECT * FROM question WHERE id != #{id} AND is_show = #{isShow} AND tag REGEXP #{tag}")
    List<Question> selectRelated(@Param("id") Long id,@Param("tag") String tag,@Param("isShow") Integer isShow);

    @Select("SELECT * FROM question WHERE  is_show = #{isShow} AND  title REGEXP #{title} order by ${sort}")
    List<Question> selectQuestionByKeyword(@Param("title") String title,@Param("isShow") Integer isShow,@Param("sort")String sort);

    @Select("SELECT id,title,comment_count,view_count FROM question ORDER BY comment_count DESC LIMIT 15")
    List<Question> selectByHot();
}
