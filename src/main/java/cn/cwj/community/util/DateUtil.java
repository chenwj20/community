package cn.cwj.community.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @Date 2020/1/29
 * @Version V1.0
 **/
public class DateUtil {
    /**
     * 获取当天0点毫秒值
     * @return
     */
    public static Long todayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取昨天0点
     * @return
     */
    public static Long yesterdayZero(){
        return todayZero()-86400000l;
    }
}
