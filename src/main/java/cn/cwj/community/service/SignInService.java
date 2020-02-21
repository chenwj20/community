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
        //首次签到
        Integer miCoin = 5;
        if (signIns == null || signIns.size() == 0){
            isSignInDTO.setMiCoin(miCoin);
            isSignInDTO.setDays(0);
            isSignInDTO.setSigned(false);
            return isSignInDTO;
        }else {
            SignIn userSignIn = signIns.get(0);
            Long gmtCreate = userSignIn.getGmtCreate();
            if (gmtCreate> DateUtil.todayZero()){
                isSignInDTO.setSigned(true);
                isSignInDTO.setDays(userSignIn.getContinueSign());
                Integer cdays = userSignIn.getContinueSign();
                miCoin = miCoin(cdays);
                isSignInDTO.setMiCoin(miCoin);
            }else {
                isSignInDTO.setSigned(false);
                isSignInDTO.setMiCoin(miCoin);
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
        //签到加经验
        userService.addExperience(uid,3);
        Integer miCoin = 5;
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
                Integer cdays = signIned.getContinueSign();
               miCoin = miCoin(cdays);
            }else {
                //昨天没签到，断签
                signIn.setContinueSign(1);
            }
            signIn.setCount(signIned.getCount()+1);

            signIn.setId(signIned.getId());
            signInMapper.updateByPrimaryKeySelective(signIn);
        }
        userService.addMiCon(uid,miCoin);
        return isSignIn(uid);
    }

    /**
     * 判断今天获得的米币
     * @param days
     * @return
     */
    private Integer miCoin(Integer days){
        Integer miCoin = 5;
        if (days<5){
            miCoin = 5;
        }else if (days<10){
            miCoin = 10;
        }else if (days<15){
            miCoin = 15;
        }else if (days<30){
            miCoin = 20;
        }else {
            miCoin = 20;
        }
        return miCoin;
    }
}
