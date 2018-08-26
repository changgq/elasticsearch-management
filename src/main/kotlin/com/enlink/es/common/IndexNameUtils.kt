package com.enlink.es.common

import java.util.*

/**
 * 获取metricbeat的索引名称
 */
fun metricbeatName(version: String) :String {
    return "metricbeat-$version-${date2ym(Date(), ".")}*";
}