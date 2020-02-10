package cn.cwj.community.service;

import cn.cwj.community.dto.IsSignInDTO;
import cn.cwj.community.mapper.SignInMapper;
import cn.cwj.community.model.SignIn;
import cn.cwj.community.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Date 2020/1/29
 * @Version V1.0
 **/
@Service
public class SignInService {
    @Autowired
    private SignInMapper signInMapper;
    @Autowired
    private UserService userService;

    /**
     * 判断用户是否签到
     * @param uid
     * @return
     */
    public IsSignInDTO isSignIn(Long uid){
        Example example = new Example(SignIn.class);
        example.createCriteria().andEqualTo("uid",uid);
        List<SignIn> signIns = signInMapper.selectByExample(example);
        IsSignInDTO isSignInDTO = new IsSignInDTO();
        //暂时设为5
        isSignInDTO.setExperience(5);
        //首次签到
        if (signIns == null || signIns.size() == 0){
            isSignInDTO.setDays(0);
            isSignInDTO.setSigned(false);
            return isSignInDTO;
        }else {
            SignIn userSignIn = signIns.get(0);
            Long gmtCreate = userSignIn.getGmtCreate();
            if (gmtCreate> DateUtil.todayZero()){
                isSignInDTO.setSigned(true);
                isSignInDTO.setDays(userSignIn.getContinueSign());
            }else {
                isSignInDTO.setSigned(false);
            }

            return isSignInDTO;
        }

    }

    /**
     * 签到
     * @param uid
     * @return
     */
    @Transactional
    public IsSignInDTO toSingIn(Long uid) {
        Example example = new Example(SignIn.class);
        example.createCriteria().andEqualTo("uid",uid);
        List<SignIn> signIns = signInMapper.selectByExample(example);

        SignIn signIn = new SignIn();

        signIn.setGmtCreate(new Date().getTime());
        userService.addExperience(uid,3);
        if (signIns == null || signIns.size() == 0){
            //第一次签到
            signIn.setUid(uid);
            signIn.setGmtModified(signIn.getGmtCreate());
            signInMapper.insertSelective(signIn);
        }else {
            SignIn signIned = signIns.get(0);
            //用户不是第一次
            signIn.setGmtModified(signIned.getGmtCreate());
            if (DateUtil.yesterdayZero()<=signIned.getGmtCreate()){
                //昨天签到了
                signIn.setContinueSign(signIned.getContinueSign()+1);
            }else {
                //昨天没签到，断签
                signIn.setContinueSign(1);
            }
            signIn.setCount(signIned.getCount()+1);

            signIn.setId(signIned.getId());
            signInMapper.updateByPrimaryKeySelective(signIn);
        }
        return isSignIn(uid);
    }
}
