package cn.cwj.community;

import cn.cwj.community.mapper.SystemuserRoleMapper;
import cn.cwj.community.model.SystemuserRole;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @Date 2020/1/30
 * @Version V1.0
 **/
public class StringTest {
    @Autowired
    private SystemuserRoleMapper systemuserRoleMapper;
    @Test
    public void test01(){
        List<SystemuserRole> select = systemuserRoleMapper.select(new SystemuserRole(1, null));
        System.out.println(select);
    }

}
