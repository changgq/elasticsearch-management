package com.enlink.es.services;

import com.enlink.es.models.ResourceLog;

import java.util.List;
import java.util.Map;

/**
 * 资源日志业务层接口
 *
 * @author changgq
 */
public interface ResourceLogService extends GeneralService<ResourceLog> {

    /**
     * 根据类型获取资源访问数量统计
     *
     * @param type day\month\year
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findResourceAccessCount(String type) throws Exception;

    /**
     * 根据类型获取用户访问数量统计
     *
     * @param type day\month\year
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findUserAccessCount(String type) throws Exception;
}
