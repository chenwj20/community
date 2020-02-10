package cn.cwj.community.enums;

public enum CommentTypeEnum {
    COMMENT_ONE(1,"一级评论"),
    COMMENT_TWO(2,"二级评论"),
    ;

    private String msg;
    private Integer val;

    public String getMsg() {
        return msg;
    }

    CommentTypeEnum( Integer val,String msg) {
        this.msg = msg;
        this.val = val;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getType() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }
}
