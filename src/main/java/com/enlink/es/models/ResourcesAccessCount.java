package com.enlink.es.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 每日资源访问量
 */
@Data
@NoArgsConstructor
public class ResourcesAccessCount {
    // 主键Id
    private String id;
    // 统计时间
    private Date timestamp;
    private String type;
    private String freq;
    // 域名
    private String domainName;
    // 访问量
    private String accessCount;
    // 资源名称
    private String resourceName;
    // 创建时间
    private Date createAt;
}
