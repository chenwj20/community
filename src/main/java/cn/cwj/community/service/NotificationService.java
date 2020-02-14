package cn.cwj.community.service;

import cn.cwj.community.enums.NotificationStatusEnum;
import cn.cwj.community.enums.NotificationTypeEnum;
import cn.cwj.community.mapper.NotificationMapper;
import cn.cwj.community.model.Notification;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/1/16
 * @Version V1.0
 **/
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 查询当前用户的回复，并分页
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Notification> findAllNotification(Long id,Integer pageNum, Integer pageSize) {
        Example example = new Example(Notification.class);

        //倒序查询
        example.setOrderByClause("gmt_create desc");
        example.createCriteria().andEqualTo("receiver",id);
        PageHelper.startPage(pageNum,pageSize);
        //消息列表
        List<Notification> notifications = notificationMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(notifications)){
            List<Long> ids = new ArrayList<>();

            for (Notification notification : notifications) {
                ids.add(notification.getId());
            }
            //更新消息状态为已读
            Example updateExample = new Example(Notification.class);
            updateExample.createCriteria()
                    .andIn("id",ids);
            Notification upNotification = new Notification();
            upNotification.setStatus(0);
            notificationMapper.updateByExampleSelective(upNotification,updateExample);
        }
        PageInfo<Notification> pageInfo = new PageInfo<>(notifications);
        return pageInfo;
    }

    /**
     * 查询未读消息
     * @param id
     * @return
     */
    public Integer findNotificationCount(Long id) {
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("receiver",id)
                .andEqualTo("status", NotificationStatusEnum.UN_READ.getStatus());
        Integer count = notificationMapper.selectCountByExample(example);

        return count;
    }

    /**
     * 删除所有消息
     * @param id
     */
    public void removeAllNotification(Long id) {
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("receiver",id);
        notificationMapper.deleteByExample(example);
    }

    /**
     * 删除指定消息
     * @param id
     */
    public void removeOneNotification(Long id) {
        notificationMapper.deleteByPrimaryKey(id);
    }

    /**
     * 消息全部已读
     * @param id
     */
    public void setNotificationStatus(Long id) {
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("receiver",id)
                                .andEqualTo("status",NotificationStatusEnum.UN_READ.getStatus());
        Notification notification = new Notification();
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByExampleSelective(notification,example);
    }

    public void setOneNotificationStatus(Long id,Long uid){
        Example example = new Example(Notification.class);
        example.createCriteria().andEqualTo("id",id)
                                .andEqualTo("receiver",uid);
        Notification notification = new Notification();
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByExampleSelective(notification,example);
    }
}
