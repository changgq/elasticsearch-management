package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统日志
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SystemLog extends GeneralModel {
    // 日志格式：WARNING|2018-08-20 14:50:47|CPU|CPU到达70%

    // 日志级别
    private String log_level;
    // 日志时间
    private Date log_time;
    // 操作方式：CPU、HDD、RAM、网卡流量、服务名称
    private String type;
    // 日志详细信息
    private String log_info;
}
