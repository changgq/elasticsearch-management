package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLog extends GeneralModel {
    // 日志级别
    private String logLevel;
    // 日志时间
    private Date logTime;
    // 操作方式：登陆、登出、消息、收藏、新建、删除、修改
    private String operationMethod;
    // 操作状态：成功、失败
    private String keywordStatus;
    // 日志描述：错误提示、成功提示等。
    private String logInfo;
    // 用户全名
    private String fullName;
    // 用户名
    private String username;
    // 用户组
    private String userGroup;
    // 登录方式
    private String loginMethod;
    // 源IP
    private String sourceIp;
    // 认证中心
    private String authCenter;
    // 连接接口
    private String linkInterface;
    // 操作系统
    private String os;
    // 冗余字段一
    private String extendFields1;
    // 冗余字段二
    private String extendFields2;
    // 冗余字段三
    private String extendFields3;
    // 冗余字段四
    private String extendFields4;
}
