package cn.cwj.community.enums;

/**
 * @Date 2020/2/6
 * @Version V1.0
 **/
public enum LVEnum {
    LV1(1,0),
    LV2(2,50),
    LV3(3,150),
    Lv4(4,300),
    Lv5(5,600),
    LV6(6,1500);
    private Integer lv;
    private Integer experience;

    LVEnum(Integer lv, Integer experience) {
        this.lv = lv;
        this.experience = experience;
    }

    public Integer getLv() {
        return lv;
    }

    public Integer getExperience() {
        return experience;
    }

    public static Integer getExperienceByLv(Integer lv) {
        for (LVEnum ele : values()) {
            if(ele.getLv().equals(lv)) return ele.getExperience();
        }
        return null;
    }

    public static void main(String[] args) {
        Integer experienceByLv = getExperienceByLv(3);
        System.out.println(experienceByLv);
    }
}
