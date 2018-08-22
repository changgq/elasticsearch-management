package com.enlink.es.services;

import com.enlink.es.models.ResourceLog;
import com.enlink.es.models.ResourcesAccessCount;
import com.enlink.es.models.UserAccessCount;

import java.util.Date;
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
     * @param day
     * @return
     * @throws Exception
     */
    List<ResourcesAccessCount> findResourceAccessCount(String type, Date day) throws Exception;

    /**
     * 根据类型获取用户访问数量统计
     *
     * @param type day\month\year
     * @param day
     * @return
     * @throws Exception
     */
    List<UserAccessCount> findUserAccessCount(String type, Date day) throws Exception;
}
