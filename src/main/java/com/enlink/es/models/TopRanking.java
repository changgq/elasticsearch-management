package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TopRanking extends GeneralModel {
    // id = md5(topic + type + cycle + key)
    // private String id;
    // 排名主题：用户登录(userlogin)、用户访问(useraccess)、资源访问(resaccess)
    private String rank_topic;
    // 排名类型：年(year)、月(month)、日(day)
    private String rank_type;
    // 排名周期：2018、2018-08、2018-08-01
    private String rank_cycle;
    // 排名名称
    private String rank_key;
    // 排名别名 默认为排名名称，资源访问统计时，通过app库匹配资源的名称。
    private String rank_key_alias;
    // 排名值
    private long rank_value;
}