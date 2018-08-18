package com.enlink.es.services;

import com.enlink.es.models.UserAccessCount;

import java.util.List;

/**
 * 用户访问统计接口
 *
 * @author changgq
 */
public interface UserAccessCountService {

    /**
     * 创建用户访问统计索引
     *
     * @throws Exception
     */
    public void createIndex() throws Exception;

    /**
     * 按周期类型查询用户访问数据top排行
     *
     * @param type
     * @param top
     * @return
     * @throws Exception
     */
    public List<UserAccessCount> findByCycleType(String type, int top) throws Exception;
}
