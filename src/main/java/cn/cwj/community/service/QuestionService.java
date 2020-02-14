package cn.cwj.community.service;

import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.dto.UserDTO;
import cn.cwj.community.enums.QuestionCategory;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.exception.CustomizeException;
import cn.cwj.community.mapper.*;
import cn.cwj.community.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Date 2020/1/13
 * @Version V1.0
 **/
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ZanMapper zanMapper;
    /**
     * 查询所有帖子列表
     * @return
     */
    public PageInfo<QuestionDTO> questionList(Integer pageNum,Integer pageSize,Integer category,String sort) {
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = null;
        if (category == null){
            Example example = new Example(Question.class);
            example.createCriteria()
                    .andEqualTo("isShow",1);
            //查询帖子
            questions = questionMapper.selectByExample(example);
        }else {
            Example example = new Example(Question.class);
            example.createCriteria()
                    .andEqualTo("category",category)
                    .andEqualTo("isShow",1);
            questions = questionMapper.selectByExample(example);
        }
        PageInfo pageInfo = new PageInfo(questions);

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        //遍历列表，获取发帖人id
        for (Question question : questions) {

            User user = userMapper.selectByPrimaryKey(question.getCreator());

            QuestionDTO questionDTO = new QuestionDTO();
            //复制属性
            BeanUtils.copyProperties(question,questionDTO);
            String str = QuestionCategory.getNameByVal(question.getCategory());
            questionDTO.setCategory(str);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        if (StringUtils.isNoneBlank(sort)){
            if (sort.equals("hot")){
                Collections.sort(questionDTOS,(o1, o2) -> o2.getGmtCreate().compareTo(o1.getGmtCreate()));
            }else {
                Collections.sort(questionDTOS,(o1, o2) -> o2.getCommentCount()-o1.getCommentCount());
            }
        }

        pageInfo.setList(questionDTOS);
        return pageInfo;
    }

    /**
     * 查询帖子及其所属用户
     * @param id
     * @return
     */
    @Transactional
    public QuestionDTO findQuestionById(Long id) {
        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("id",id)
                .andEqualTo("isShow",1);
        Question question = questionMapper.selectOneByExample(example);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //更新查看人数
        incView(id);
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setCategoryI(question.getCategory());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 创建或者帖子
     * @param question
     */
    @Transactional
    public void createOrUpdateQuestion(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //创建帖子
            questionMapper.insertSelective(question);
            userService.addExperience(question.getCreator(),1);
        }else {
            //更新帖子
            question.setGmtModified(System.currentTimeMillis());
            int num = questionMapper.updateByPrimaryKeySelective(question);
            if (num == 0){
                throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
            }
        }
    }

    /**
     * 添加查看人数
     * @param id
     */
    public void incView(Long id){
        Question updateQuestion = new Question();
        Question question = questionMapper.selectByPrimaryKey(id);
        updateQuestion.setViewCount(question.getViewCount()+1);
        Example example = new Example(Question.class);
        example.createCriteria().andEqualTo("id",id);
        questionMapper.updateByExampleSelective(updateQuestion,example);

    }

    /**
     * 根据问题评论数排行查询你问题
     */
    public List<Question> findByComments(Integer pageSize,Integer pageNum) {
        Example example = new Example(Question.class);
        example.setOrderByClause("comment_count desc");
        example.createCriteria().andEqualTo("isShow",1);

        PageHelper.startPage(pageSize,pageNum);
        List<Question> questions = questionMapper.selectByExample(example);
        return questions;
    }

    /**
     *彻底删除帖子
     * @param id
     * @param user
     */
    @Transactional
    public void deleteQuestion(Long id,User user) {
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }else {
            Question question = questionMapper.selectByPrimaryKey(id);
            if (question.getCreator()!=user.getId()){
                throw new CustomizeException(CustomizeErrorCode.INVALID_DELETE);
            }
        }
        Example example = new Example(Comment.class);
        example.createCriteria().andEqualTo("parentId",id);
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments == null || comments.size() == 0){
            questionMapper.deleteByPrimaryKey(id);
        }else {
            commentMapper.deleteByExample(example);
            questionMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据tag查询问题
     * @param tag
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<QuestionDTO> findQuestionByTag(String tag, Integer pageNum, Integer pageSize) {
        Example example = new Example(Question.class);
        StringBuffer sb = new StringBuffer();
        sb.append("%");
        sb.append(tag);
        sb.append("%");
        example.createCriteria()
                .andLike("tag",sb.toString())
                .andEqualTo("isShow",1);
        System.out.println(tag);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.selectByExample(example);
        System.out.println("tag:"+questions);
        PageInfo pageInfo = new PageInfo(questions);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {

            User user = userMapper.selectByPrimaryKey(question.getCreator());

            QuestionDTO questionDTO = new QuestionDTO();
            //复制属性
            BeanUtils.copyProperties(question,questionDTO);
            String str = QuestionCategory.getNameByVal(question.getCategory());
            questionDTO.setCategory(str);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageInfo.setList(questionDTOS);
        return pageInfo;
    }

    public List<Question> selectRelated(QuestionDTO questionDTO) {
        String tag = questionDTO.getTag();
        String regexTag = tag.replace("+", "").replace("*", "").replace("?", "").replace(",","|");

//        String regexTag = StringUtils.join(tags, "|");
        System.out.println(regexTag);
        PageHelper.startPage(1,10);
        List<Question> relatedQuestions = questionMapper.selectRelated(questionDTO.getId(),regexTag,1);
        return relatedQuestions;
    }

    /**
     * 移除问题放入，隐藏
     * @param id
     * @param user
     */
    public void removeQuestion(Long id, User user) {
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Question question = questionMapper.selectByPrimaryKey(id);

        if (question.getCreator()!=user.getId()){
            throw new CustomizeException(CustomizeErrorCode.INVALID_DELETE);
        }

        question.setIsShow(0);
        questionMapper.updateByPrimaryKeySelective(question);
    }

    public PageInfo findRecycle(Long uid,Integer pageNum,Integer pageSize) {
        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("isShow",0)
                .andEqualTo("creator",uid);
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions = questionMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(questions);
        return pageInfo;
    }

    public List<User> findCollectionUser(Long qid) {
        Example example = new Example(Collect.class);
        example.setOrderByClause("gmt_create desc");
        example.createCriteria()
                .andEqualTo("questionId",qid);
        List<Collect> collects = collectMapper.selectByExample(example);
        List<User> users = new ArrayList();
        for (Collect collect : collects) {
            User user = userMapper.selectByPrimaryKey(collect.getUserId());
            users.add(user);
        }
        return users;
    }

    public List<User> findZanUser(Long id) {
        Example example = new Example(Zan.class);
        example.createCriteria()
                .andEqualTo("parentId",id);
        List<Zan> zans = zanMapper.selectByExample(example);
        List<User> users = new ArrayList();
        for (Zan zan : zans) {
            User user = userMapper.selectByPrimaryKey(zan.getUid());
            users.add(user);
        }
        return users;

    }

    /**
     * 根据帖子状态查询
     * @return
     */
    public List<QuestionDTO> findByStatus(Integer status) {
        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("status",status)
                .andEqualTo("isShow",1);
        PageHelper.startPage(1,3);
        List<Question> questions = questionMapper.selectByExample(example);
        ArrayList<QuestionDTO> statusQuestionList = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            BeanUtils.copyProperties(question,questionDTO);
            String str = QuestionCategory.getNameByVal(question.getCategory());
            questionDTO.setCategory(str);
            questionDTO.setUser(user);
            statusQuestionList.add(questionDTO);
        }
        return statusQuestionList;
    }

    /**
     * 回收帖子
     * @param id
     * @param user
     */
    public void recycleQuestion(Long id, User user) {
        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("creator",user.getId())
                .andEqualTo("id",id)
                .andEqualTo("isShow",0);
        Question question = new Question();
        question.setIsShow(1);
        questionMapper.updateByExampleSelective(question,example);
    }
    @Transactional
    public void recycleOrDeleteAllQuestion(User user,Integer type){
        Example example = new Example(Question.class);
        example.createCriteria()
                .andEqualTo("creator",user.getId())
                .andEqualTo("isShow",0);
        if (type == 1){
            List<Question> questions = questionMapper.selectByExample(example);
            List<Long> ids = new ArrayList<>();
            for (Question question : questions) {
                ids.add(question.getId());
            }
            Example commentExample = new Example(Comment.class);
            commentExample.createCriteria().andIn("parentId",ids);
            commentMapper.deleteByExample(commentExample);
            questionMapper.deleteByExample(example);
        }else {
            Question question = new Question();
            question.setIsShow(1);
            //种类0为回收帖子
            questionMapper.updateByExampleSelective(question,example);
        }
    }

    /**
     * 置顶问题
     * @param id
     */
    public void topQuestion(Long id) {
        Question question = new Question();
        question.setStatus(1);
        question.setId(id);
        questionMapper.updateByPrimaryKeySelective(question);
    }
}
