package com.enlink.es.services;

import com.enlink.es.models.UserLog;
import com.enlink.es.models.UserLoginCount;

import java.util.Date;
import java.util.List;

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
     * @param day
     * @return
     * @throws Exception
     */
    List<UserLoginCount> getUserLoginCount(String type, Date day) throws Exception;
}
