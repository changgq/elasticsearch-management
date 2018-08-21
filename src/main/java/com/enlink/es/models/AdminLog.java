package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 管理员日志
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminLog extends GeneralModel {
    // 日志格式：INFO|2018-07-03 08:57:10|登录管理端|10|zhoujing||普通管理员|192.168.5.182|43027782-a8d0-4556-b4d0-1caeef9b3173|成功|

    // 日志级别
    private String log_level;
    // 日志时间
    private String log_time;
    // 操作方式
    private String operation_method;
    // 全名
    private String full_name;
    // 用户名
    private String user_name;
    // 角色：由用户组字段替
    private String role_name;
    // 权限名称：由原来角色字段替换
    private String right_name;
    // 源ip
    private String remote_ip;
    // MAC地址
    private String mac_address;
    // 日志详细信息
    private String log_info;

}
