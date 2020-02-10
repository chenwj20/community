package cn.cwj.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "请先登陆"),
    NO_LOGIN1(0, "请先登陆"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    INVALID_INPUT(2011, "非法输入"),
    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    INVALID_DELETE(2013,"非法删除"),
    SIGN_IN_ERROR(2014,"签到错误"),
    VERIFY_CODE_ERROR(2015,"验证码错误"),
    USER_VERIFY_ERROR(2016,"用户名或密码错误"),
    EMAIL_CODE_ERROR(2017,"邮箱验证码错误"),
    EMAIL_CODE_OVERDUE(2018,"邮箱验证码已过期"),
    USER_IS_EXIST(2019,"用户名已存在"),
    TAG_IS_EXIST(2020,"标签已存在"),
    EMAIL_IS_EXIST(2021,"该邮箱已存在"),
    EMAIL_UNREG(2022,"该邮箱尚未注册，请先注册"),
    TAG_ONE(2023,"最少添加一个标签"),
    NOW_PASSWORD_EEROR(2024,"当前密码输入错误"),
    PASSWORD_FORMAT_EEROR(2025,"请输入正确的密码格式"),
    PASSWORD_DIFFERENCE_EEROR(2026,"密码不一致");

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
