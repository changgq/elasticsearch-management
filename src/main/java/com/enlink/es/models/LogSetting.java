package com.enlink.es.models;

import com.enlink.es.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * 日志设置
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LogSetting extends GeneralModel {
    // 主键
    private String id;
    // 是否启用第三方数据库
    private boolean use_third_db;
    // 日志清理类型，按日期、按数量
    private String config_type;
    // 日志设置的开始时间（yyyy-MM-dd日期格式）
    private String start_save_date;
    // 时间周期
    private long day_rate = 90;
    // 条数周期，默认10000
    private long count_rate = 10000;
    // 已删除日志的最后一天
    private String last_delete_date;
    // 最后一次日志备份日期
    private String last_backups_date;
    // 日志模块开启状态，默认为false
    private boolean start_status;
    // 日志类别，默认所有
    private List<String> log_types;
    // 日志级别，默认所有
    private List<String> log_levels;
    // 最后一次统计日期
    private String last_count_date;

    /**
     * 返回初始化默认值的对象
     *
     * @return
     */
    public LogSetting initial() {
        LogSetting logSetting = new LogSetting();
        logSetting.setId("1");
        logSetting.setUse_third_db(false);
        logSetting.setConfig_type("DATE");
        logSetting.setStart_save_date(DateUtils.date2string(new Date()));
        logSetting.setDay_rate(90);
        logSetting.setCount_rate(10000);
        logSetting.setLast_delete_date(DateUtils.date2string(new Date()));
        logSetting.setLast_backups_date(DateUtils.date2string(new Date()));
        logSetting.setStart_status(true);
        logSetting.setLog_types(Arrays.asList("RES", "USER", "ADMIN", "SYSTEM"));
        logSetting.setLog_levels(Arrays.asList("INFO", "WARNING", "ERROR"));
        logSetting.setLast_count_date(DateUtils.date2string(new Date()));
        return logSetting;
    }
}
