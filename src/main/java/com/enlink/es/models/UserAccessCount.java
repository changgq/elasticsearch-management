package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserAccessCount extends GeneralModel {
    // 主键id = md5(cycle + username + userGroup + fullName)
    private String id;
    // 统计类型：最近（day）、本月（month）、本年（year）
    private String count_type;
    // 根据统计类型，分别记录不同的内容，例如：
    // if (countType == "day") cycle = "2018-08-20";
    // if (countType == "month") cycle = "2018-08";
    // if (countType == "year") cycle = "2018";
    private String cycle;
    // 用户名
    private String username;
    // 用户组
    private String user_group;
    // 全名
    private String full_name;
    // 访问次数
    private long access_count;
}
