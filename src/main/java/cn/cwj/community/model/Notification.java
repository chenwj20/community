package cn.cwj.community.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Date 2020/1/16
 * @Version V1.0
 * 通知
 **/
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long notifier;
    private Long receiver;
    private Integer type;
    private Long outerId;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private Long commentId;
    private String commentContent;
    @Transient
    private Long duringTime;

}
