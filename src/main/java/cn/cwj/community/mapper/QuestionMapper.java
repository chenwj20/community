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
}
