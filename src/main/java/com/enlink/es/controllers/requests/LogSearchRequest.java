package com.enlink.es.controllers.requests;

import lombok.Data;

@Data
public class LogSearchRequest {
    private String date;
    private String startTime;
    private String endTime;
    private int pageIndex;
    private int pageSize;
    // ====通用查询条件====
    private String userInfo;
    private String authCenter;
    private String remoteIp;
    private String operationMethod;
    private String logInfo;
    // ====管理员日志====
    private String rightName;
    // ====系统日志====
    private String logLevel;
    // ====资源日志====
    private String appType;
}
