package cn.cwj.community.service;

import cn.cwj.community.dto.CollectionQuestionDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.dto.UserOwnDTO;
import cn.cwj.community.enums.LVEnum;
import cn.cwj.community.mapper.CollectMapper;
import cn.cwj.community.mapper.CommentMapper;
import cn.cwj.community.mapper.QuestionMapper;
import cn.cwj.community.mapper.UserMapper;
import cn.cwj.community.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.*;

import static cn.cwj.community.enums.LVEnum.LV1;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CollectMapper collectMapper;

    public void insert(User user) {
        userMapper.insertSelective(user);
    }

    //根据用户名密码查询之后再做优化
    public User findUserByEmailAndPassword(String email, String password) {
        //初始化example对象
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email",email).andEqualTo("password",password);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }
    public UserDTO findByIdMore(Long id) {
        UserDTO userDTO = new UserDTO();
        User user = userMapper.selectByPrimaryKey(id);
        //查询该用户发布的问题
        Example questionExample = new Example(Question.class);
        questionExample.createCriteria()
                .andEqualTo("creator",id)
                .andEqualTo("isShow",1);
        PageHelper.startPage(1,15);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        //查询该用户的回答
        Example commentExample = new Example(Comment.class);
        commentExample.createCriteria().andEqualTo("commentator",id);
        PageHelper.startPage(1,15);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<Comment> showComments = new ArrayList<>();
        for (Comment comment : comments) {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question.getIsShow() == 0){
                continue;
            }

            showComments.add(comment);
        }
        BeanUtils.copyProperties(user,userDTO);
        userDTO.setQuestions(questions);
        userDTO.setComments(showComments);
        return userDTO;
    }
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
    //根据token查询用户
    public User findByToken(String token) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("token",token);
        List<User> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    public List<UserDTO> findByCommentCount() {
        PageHelper.startPage(1,20);
        List<User> users = userMapper.selectAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            Integer commentCount = commentMapper.selectCountByCommentator(user.getId());
            BeanUtils.copyProperties(user,userDTO);
            userDTO.setCommentCounts(commentCount);
            userDTOS.add(userDTO);
        }
        Collections.sort(userDTOS, ((o1, o2) -> o2.getCommentCounts() - o1.getCommentCounts()));
        return userDTOS;
    }

    /**
     * 查询用户收藏的帖子,
     * @param id
     */
    public PageInfo<CollectionQuestionDTO> findMyCollection(Long id,Integer pageNum,Integer pageSize) {
        Example collectExample = new Example(Collect.class);
        collectExample.createCriteria().andEqualTo("userId",id);
        //分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Collect> collects = collectMapper.selectByExample(collectExample);
        PageInfo pageInfo = new PageInfo(collects);
        if (collects == null || collects.size() == 0){

        }
//        List<Long> questionIds = new ArrayList<>();
        List<CollectionQuestionDTO> collectionQuestionDTOS = new ArrayList<>();
        int count = 0;
        for (Collect collect : collects) {
            CollectionQuestionDTO collectionQuestionDTO = new CollectionQuestionDTO();
            Example example = new Example(Question.class);
            example.createCriteria()
                    .andEqualTo("id",collect.getQuestionId())
                    .andEqualTo("isShow",1);
            Question question = questionMapper.selectOneByExample(example);
            if (question != null){
                count+=1;
                BeanUtils.copyProperties(question,collectionQuestionDTO);
                collectionQuestionDTO.setCollectionTime(collect.getGmtCreate());
                collectionQuestionDTOS.add(collectionQuestionDTO);
            }

        }
        pageInfo.setTotal(count);
        pageInfo.setList(collectionQuestionDTOS);
        return pageInfo;
    }

    /**
     * 查询用户发的帖子
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Question> findMyQuestions(Long id, Integer pageNum, Integer pageSize) {

        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("creator",id)
                .andEqualTo("isShow",1);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.selectByExample(example);
        PageInfo<Question> myQuestions = new PageInfo<>(questions);
        return myQuestions;

    }

    /**
     * 查询用户是否存在
     * @param user
     * @return
     */
    public Boolean checkUser(User user) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("name", user.getName());
        if (user.getId() != null){
            criteria.andNotEqualTo("id",user.getId());
        }
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            return false;
        }
        return true;
    }

    public void createOrUpdateUser(User user) {
        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo("accountType",user.getAccountType())
                .andEqualTo("accountId",user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            //新增加用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
            userMapper.insertSelective(user);
        }else {
            //更新用户
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setBio(user.getBio());
            Example updateExample = new Example(User.class);
            updateExample.createCriteria().andEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,updateExample);
        }
    }

    public User findLvById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    public void userEdit(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }


    public void addExperience(Long id,Integer type){
        User user = userMapper.selectByPrimaryKey(id);
        Integer addExperience = 0;
        switch (type) {
            case 1:
                addExperience=10;
                break;
            case 2:
                addExperience=2;
                break;
            case 3:
                addExperience=5;
                break;
        }
        user.setExperience(user.getExperience()+addExperience);
        Integer experience = user.getExperience();
        if (experience>1500){
            user.setLv(6);
        }else if (experience>600){
            user.setExperience(5);
        }else if (experience>300){
            user.setLv(4);
        }else if (experience>150){
            user.setLv(3);
        }else if (experience>50){
            user.setLv(2);
        }else {
            user.setLv(1);
        }
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 查询用户还有多久升级
     * @param userDTO
     * @return
     */
    public UserOwnDTO findUserEx(UserDTO userDTO) {
        Integer experience = userDTO.getExperience();
        Integer lv = userDTO.getLv();
        Integer nextExperience = LVEnum.getExperienceByLv(lv + 1);
        UserOwnDTO userOwnDTO = new UserOwnDTO();
        if (nextExperience != null){
            double num = (double)experience/nextExperience;
            DecimalFormat df = new DecimalFormat("0.00%");
            String s = df.format(num);
            userOwnDTO.setPercentage(s);
        }
        userOwnDTO.setExperience(experience);
        userOwnDTO.setNextExperience(nextExperience);
        return userOwnDTO;
    }


    public boolean checkUserByEmail(User user) {
        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo("email",user.getEmail());
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            return false;
        }
        return true;
    }

    public User finUserByEmail(String email) {
        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo("email",email);
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            return null;
        }
        return users.get(0);
    }

    /**
     * 更新用户token
     * @param user
     */
    public void updateUserToken(User user){
        userMapper.updateByPrimaryKeySelective(user);
    }
}
