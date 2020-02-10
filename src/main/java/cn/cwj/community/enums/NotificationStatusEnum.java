package cn.cwj.community.enums;

public enum NotificationStatusEnum {
    READ(0,"已读"),
    UN_READ(1,"未读");

    NotificationStatusEnum( Integer status,String desc) {
        this.status = status;
        this.desc = desc;
    }

    private Integer status;

    private  String desc;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
