package cn.cwj.community.enums;

public enum CommonEnum {
    COMMENT_SUCCESS(1000,"评论成功"),
    SIGNEDINED(1002,"已经签到过了"),
    DELETE_SUCCESS(1003,"删除成功"),
    ZAN_SUCCESS(1004,"点赞成功"),
    CANCEL_SUCCESS(1005,"取消成功"),
    TAG_ADD__SUCCESS(1006,"标签创建成功"),
    LV_LOWER(1007,"您的等级还不够"),
    AVATAR_SUCCESS(1008,"头像更改成功"),
    SET_SUCCESS(1009,"修改成功"),
    RECYCLE_SUCCESS(1010,"恢复成功"),
    Top_question__SUCCESS(1011,"置顶成功"),
    MICOIN_LESS(1012,"米币不足"),
    ACCEPT__SUCCESS(1013,"采纳成功"),
    PASSWORD_TRUE(1014,"口令正确"),
    ADD_SUCCESS(1015,"添加成功"),
    BAN_SUCCESS(1016,"封禁成功"),
    NOBAN_SUCCESS(1017,"解禁成功"),
    FINE_SUCCESS(1018,"置精成功"),
    OPTION__SUCCESS(1019,"操作成功");



    private Integer code;
    private String message;

    CommonEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
