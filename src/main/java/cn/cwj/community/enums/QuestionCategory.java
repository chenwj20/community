package cn.cwj.community.enums;

/**
 * @Date 2020/2/1
 * @Version V1.0
 **/

public enum  QuestionCategory {
    NOTICE(1,"公告"),
    Put_Questions(2,"提问"),
    Share(3,"分享"),
    Discuss(4,"讨论"),
    Advise(5,"建议"),
    News(6,"新闻"),
    Bug(7,"Bug"),
    INTERVIEW(8,"面试");
    private Integer value;
    private  String name;

    QuestionCategory(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByVal(Integer value){
        QuestionCategory[] values = QuestionCategory.values();
        for (Integer i = 0; i < value; i++) {
            if(values[i].getValue()==value){
                return values[i].name;
            }
        }
        return "";
    }
    public Integer getValue() {
        return value;
    }


    public String getName() {
        return name;
    }

}

