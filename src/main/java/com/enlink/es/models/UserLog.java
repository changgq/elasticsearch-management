package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserLog extends GeneralModel {
    // 日志格式：INFO|2018-08-20 14:50:47|MSG|READ|在Windows有登陆，若非本人操作，请及时修改密码|跋山涉水|zaf|wyq|web|117.89.135.200:36789||interface 1|Windows||web app v3.0||

    // 日志级别
    private String log_level;
    // 日志时间
    private Date log_time;
    // 操作方式：登陆、登出、消息、收藏、新建、删除、修改
    // LOGIN、LOGOUT、MSG、。。。。
    private String operation_method;
    // 操作状态：成功、失败、阅读、接收、收藏、取消收藏
    // SUCCESS、FAIL、READ、RECEIVE、
    private String keyword_status;
    // 日志描述：错误提示、成功提示等。
    private String log_info;
    // 用户全名
    private String full_name;
    // 用户名
    private String user_name;
    // 用户组
    private String user_group;
    // 登录方式
    private String login_method;
    // 源IP
    private String remote_ip;
    // 认证中心
    private String auth_center;
    // 连接接口
    private String link_interface;
    // 操作系统
    private String os;
    // 冗余字段一
    private String extend_fields_1;
    // 冗余字段二
    private String extend_fields_2;
    // 冗余字段三
    private String extend_fields_3;
}
