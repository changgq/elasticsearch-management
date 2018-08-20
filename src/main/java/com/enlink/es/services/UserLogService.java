package com.enlink.es.services;

import com.enlink.es.models.UserLog;

import java.util.List;
import java.util.Map;

/**
 * 用户日志业务层接口
 *
 * @author changgq
 */
public interface UserLogService extends GeneralService<UserLog> {

    /**
     * 根据类型统计用户登录次数
     *
     * @param type
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getUserLoginCount(String type) throws Exception;
}
