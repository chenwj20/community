package cn.cwj.community.service;

import cn.cwj.community.dto.CommentDTO;
import cn.cwj.community.dto.CommentDTOU;
import cn.cwj.community.dto.QuestionDTO;
import cn.cwj.community.enums.CommentTypeEnum;
import cn.cwj.community.enums.NotificationStatusEnum;
import cn.cwj.community.enums.NotificationTypeEnum;
import cn.cwj.community.exception.CustomizeErrorCode;
import cn.cwj.community.exception.CustomizeException;
import cn.cwj.community.mapper.*;
import cn.cwj.community.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2020/1/15
 * @Version V1.0
 **/
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ZanMapper zanMapper;
    @Autowired
    private UserService userService;
    //根据parentId查找评论
    public PageInfo findByParentId(Long id, Integer type, Integer pageNum, Integer pageSize, User wuser){
        Question question = questionMapper.selectByPrimaryKey(id);
        Example commentExample = new Example(Comment.class);
        if (question.getAcceptId() != null){
            commentExample.setOrderByClause("id="+question.getAcceptId()+" desc");
        }
        commentExample.createCriteria()
                .andEqualTo("parentId",id);
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        PageInfo pageInfo = new PageInfo(comments);

        if (comments == null || comments.size() == 0){
            return pageInfo;
        }
        //用户id去重
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论的人，并转换为map

        Example userExample = new Example(User.class);
        userExample.createCriteria().andIn("id",userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user ->user.getId(),user -> user));

        //转换 comment 为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
      /*  Collections.sort(commentDTOS, new Comparator<CommentDTO>() {
            @Override
            public int compare(CommentDTO o1, CommentDTO o2) {
                return o2.getGmtCreate() - o1.getGmtCreate();
            }
        });*/
        if (wuser !=null) {
            List<CommentDTOU> commentDTOUS = new ArrayList<>();

            for (CommentDTO commentDTO : commentDTOS) {
                CommentDTOU commentDTOU = new CommentDTOU();
                Example zanExample = new Example(Zan.class);
                Example.Criteria criteria = zanExample.createCriteria()
                        .andEqualTo("uid", wuser.getId())
                        .andEqualTo("type", 2)
                        .andEqualTo("parentId", commentDTO.getId());
                List<Zan> zans = zanMapper.selectByExample(zanExample);
                System.out.println(zans);
                BeanUtils.copyProperties(commentDTO, commentDTOU);
                commentDTOU.setFlag(!CollectionUtils.isEmpty(zans));
                commentDTOUS.add(commentDTOU);
            }
            pageInfo.setList(commentDTOUS);
        } else {

            pageInfo.setList(commentDTOS);
        }

//        System.out.println(pageInfo);
        return pageInfo;
    }

    /**
     * 新增评论
     * @param comment
     */
    @Transactional
    public Long insertComment(Comment comment,User user,Long toCommentId) {
        if (comment.getParentId() == null){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null ){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //二级回复
        if (comment.getType() == CommentTypeEnum.COMMENT_TWO.getType()){
            Comment questionComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (questionComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(questionComment.getParentId());
            // 创建通知
            createNotify(comment, questionComment.getCommentator(), user.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            if (toCommentId != -1){
                User userComment = userMapper.selectByPrimaryKey(toCommentId);
                StringBuffer sb = new StringBuffer("@");
                //<a href='/user/home/' class='fly-aite'>七月的尾巴</a>
                sb.append("<a href='/user/home/");
                sb.append(toCommentId);
                sb.append("' class='fly-aite'>");
                sb.append(userComment.getName());
                sb.append(" </a>");
                sb.append(comment.getContent());
                String commentContent = sb.toString();
                comment.setContent(commentContent);
                comment.setType(CommentTypeEnum.COMMENT_TWO.getType());
                userService.addExperience(user.getId(),2);
                createNotify(comment,toCommentId,user.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());
            }
            comment.setParentTitle(question.getTitle());
            commentMapper.insertSelective(comment);

            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.updateByPrimaryKeySelective(question);

            // 创建通知

            createNotify(comment, question.getCreator(), user.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());

        }
        return comment.getId();
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        /*if (receiver == comment.getCommentator()) {
            return;
        }*/
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UN_READ.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setCommentId(comment.getId());
        String content = comment.getContent();

        if (content.length()>20){
            String notifyContent = content.substring(0, 20);
            notification.setCommentContent(notifyContent);
        }else {
            notification.setCommentContent(content);
        }
        notificationMapper.insert(notification);
    }

    public void commentZan(Long id, Long uid) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        comment.setLikeCount(comment.getLikeCount()+1);

    }
    @Transactional
    public void removeComment(Long id, Long ParentId,Long uid) {

        Question question = questionMapper.selectByPrimaryKey(ParentId);
        question.setCommentCount(question.getCommentCount()-1);
        questionMapper.updateByPrimaryKeySelective(question);
        commentMapper.deleteByPrimaryKey(id);
    }
}
