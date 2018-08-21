package com.enlink.es.tasks;


import com.enlink.es.models.UserLoginCount;
import com.enlink.es.services.*;
import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 每日凌晨（0点）执行定时任务
 *
 * @author changgq
 */
@Slf4j
@Component
public class DailyTask {

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private ResourceLogService resourceLogService;

    @Autowired
    private UserLoginCountService userLoginCountService;

    @Autowired
    private UserAccessCountService userAccessCountService;

    @Autowired
    private ResourcesAccessCountService resourcesAccessCountService;

    @Async
    @Scheduled(cron = "0 0 0 * * ?") // 凌晨1点：0 0 1 * * ?
    public void run() {
        LOGGER.info(DateUtils.datetime2string(new Date()) + " : Daily count task start......");
        userLoginDaily();
        userAccessDaily();
        resourcesAccessDaily();
        LOGGER.info(DateUtils.datetime2string(new Date()) + " : Daily count task end !");
    }


    /**
     * 按天统计用户登录次数
     */
    private void userLoginDaily() {
        try {
            // 获取最近一天的日志登录数统计情况
            List<UserLoginCount> dailyUserLoginCount = userLogService.getUserLoginCount("day");
            for (UserLoginCount m : dailyUserLoginCount) {

            }
            // 获取最近一月的日志登录数统计情况
            List<UserLoginCount> monthUserLoginCount = userLogService.getUserLoginCount("month");
            for (UserLoginCount m : monthUserLoginCount) {

            }
            // 获取最近一年的日志登录数统计情况
            List<UserLoginCount> yearUserLoginCount = userLogService.getUserLoginCount("year");
            for (UserLoginCount m : yearUserLoginCount) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按天统计用户访问量
     */
    private void userAccessDaily() {

    }

    /**
     * 按天统计资源访问量
     */
    private void resourcesAccessDaily() {

    }
}
