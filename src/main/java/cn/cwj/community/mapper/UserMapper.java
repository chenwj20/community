package cn.cwj.community.mapper;

import cn.cwj.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Date 2020/1/12
 * @Version V1.0
 **/
@Repository
public interface UserMapper extends Mapper<User> {
/*    @Insert("INSERT INTO USER(id,account_id,account_type,NAME,token,gmt_create,gmt_modified,avatar_url,bio,location,company,phone,PASSWORD,email)\n" +
            "VALUES(#{id},#{accountId},#{accountType},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl},#{bio},#{location},#{company},#{phone},#{password},#{email})")
    void insertUser(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Select("select * from user where id =#{id}")
    User findById(@Param("id") Long id);

    @Select("select * from user where phone = #{phone} and password = #{password}")
    User findUserByPhoneAndPassword(@Param("phone")String phone, @Param("password") String password);*/
}
