package com.enlink.es.base;

import com.enlink.es.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 周期统计数据类型
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
public class CountCycle {
    // 排行榜主题 用户登录次数：user-login、用户访问次数: user-access、资源访问次数: res-access
    private String countTopic;
    // 统计类型 day、month、year
    private String countType;
    // 周期值 2018-01-01、2018-01、2018
    private String cycle;
    // 格式 yyyy-MM-dd、yyyy-MM、yyyy
    private String pattern;
    // 周期起始日期，格式：yyyy-MM-dd
    private String from;
    // 周期结束日期，格式：yyyy-MM-dd
    private String to;


    /**
     * 根据类型和日期获取周期对象
     *
     * @param count_type
     * @param date
     * @return
     */
    public static CountCycle getCountCycle(String topic, String count_type, Date date) {
        Date basicDay = null == date ? DateUtils.getYesterday() : date;
        CountCycle cyclePojo = new CountCycle();
        cyclePojo.setCountTopic(topic);
        cyclePojo.setCountType(count_type);
        cyclePojo.setPattern("yyyy-MM-dd");
        if ("month".equals(count_type)) {
            cyclePojo.setCycle(DateUtils.date2monthstring(basicDay));
            cyclePojo.setFrom(DateUtils.date2string(DateUtils.firstDayOfMonth(basicDay)));
            cyclePojo.setTo(DateUtils.date2string(DateUtils.lastDayOfMonth(basicDay)));
        } else if ("year".equals(count_type)) {
            cyclePojo.setCycle(DateUtils.date2yearstring(basicDay));
            cyclePojo.setFrom(DateUtils.date2string(DateUtils.firstDayOfYear(basicDay)));
            cyclePojo.setTo(DateUtils.date2string(DateUtils.lastDayOfYear(basicDay)));
        } else {
            cyclePojo.setCycle(DateUtils.date2string(basicDay));
            cyclePojo.setFrom(DateUtils.date2string(basicDay));
            cyclePojo.setTo(DateUtils.date2string(basicDay));
        }
        return cyclePojo;
    }
}
