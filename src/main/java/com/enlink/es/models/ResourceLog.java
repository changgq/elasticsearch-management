package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源日志
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResourceLog extends GeneralModel {
    // 日志格式：INFO|bdd254cca0ee23fe7777a98dac99d2a3|1036|full|gy|192.168.5.150:63523|400|2018-04-23 18:05:43|0.010|192.168.32.212/bbs|192.168.32.212/bbs/bbs/forum.php|562|716|1278|Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36|https://client.test.casb1.com/client/|1|GET /bbs/forum.php HTTP/1.1|text/html|Enwas|
    // "%{DATA:casbs.res.keyword_log_level}\\|%{DATA:casbs.res.session_id}\\|%{DATA:casbs.res.user_id}\\|%{DATA:casbs.res.user_name}\\|%{DATA:casbs.res.user_group}\\|%{DATA:casbs.res.ip_address}\\|%{DATA:casbs.res.keyword_status}\\|%{DATA:casbs.res.log_timestamp}\\|%{DATA:casbs.res.float_response_time}\\|%{DATA:casbs.res.resource_name}\\|%{DATA:casbs.res.uri}\\|%{NUMBER:casbs.res.long_uplink_traffic}\\|%{NUMBER:casbs.res.long_downlink_traffic}\\|%{NUMBER:casbs.res.long_total_traffic}\\|%{DATA:casbs.res.browser_info}\\|%{DATA:casbs.res.request_referer}\\|%{DATA:casbs.res.short_request_count}\\|%{DATA:casbs.res.url_http}\\|%{DATA:casbs.res.keyword_file_format}\\|%{DATA:casbs.res.app_type}\\|"

    // 日志级别
    private String log_level;
    // sessionId
    private String session_id;
    // 全名
    private String full_name;
    // 用户名
    private String user_name;
    // 用户组
    private String user_group;
    // 源ip
    private String remote_ip;
    // 2**、3**、4**、5**
    private String keyword_status;
    // 日志时间
    private Date log_time;
    // 响应时间
    private float response_time;
    // 资源名称
    private String resource_name;
    // 统一资源标识符（Uniform Resource Identifier）
    private String uri;
    // 上行流量
    private long upward_flow;
    // 下行流量
    private long downward_flow;
    // 总流量
    private long total_flow;
    // User-Agent：用户请求的代理软件，包括用户的操作系统，浏览器等相关属性。
    private String user_agent_string;
    // 请求报头；请求首部
    private String request_referer;
    // 最小请求次数?
    private String request_count;
    // http协议
    private String http_protocol;
    // Content-Type：响应实体的内容类型
    private String content_type;
    // 客户端类型
    private String app_type;
}
