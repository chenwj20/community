package cn.cwj.community.mapper;


import cn.cwj.community.model.SystemUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SystemUserMapper extends Mapper<SystemUser> {

}
