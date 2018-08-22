package com.enlink.es.tasks;


import com.enlink.es.models.ResourcesAccessCount;
import com.enlink.es.models.UserAccessCount;
import com.enlink.es.models.UserLoginCount;
import com.enlink.es.services.*;
import com.enlink.es.utils.DateUtils;
import com.google.gson.GsonBuilder;
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
    @Scheduled(cron = "0 */2 * * * ?") // 凌晨1点：0 0 0 * * ?
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
            Date day = DateUtils.getYesterday();
            // 获取最近一天的日志登录数统计情况
            List<UserLoginCount> dailyCounts = userLogService.getUserLoginCount("day", day);
            for (UserLoginCount m : dailyCounts) {
                userLoginCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一月的日志登录数统计情况
            List<UserLoginCount> monthCounts = userLogService.getUserLoginCount("month", day);
            for (UserLoginCount m : monthCounts) {
                userLoginCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一年的日志登录数统计情况
            List<UserLoginCount> yearCounts = userLogService.getUserLoginCount("year", day);
            for (UserLoginCount m : yearCounts) {
                userLoginCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按天统计用户访问量
     */
    private void userAccessDaily() {
        try {
            Date day = DateUtils.getYesterday();
            // 获取最近一天的日志登录数统计情况
            List<UserAccessCount> dailyCounts = resourceLogService.findUserAccessCount("day", day);
            for (UserAccessCount m : dailyCounts) {
                userAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一月的日志登录数统计情况
            List<UserAccessCount> monthCounts = resourceLogService.findUserAccessCount("month", day);
            for (UserAccessCount m : monthCounts) {
                userAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一年的日志登录数统计情况
            List<UserAccessCount> yearCounts = resourceLogService.findUserAccessCount("year", day);
            for (UserAccessCount m : yearCounts) {
                userAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按天统计资源访问量
     */
    private void resourcesAccessDaily() {
        try {
            Date day = DateUtils.getYesterday();
            // 获取最近一天的日志登录数统计情况
            List<ResourcesAccessCount> dailyCounts = resourceLogService.findResourceAccessCount("day", day);
            for (ResourcesAccessCount m : dailyCounts) {
                resourcesAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一月的日志登录数统计情况
            List<ResourcesAccessCount> monthCounts = resourceLogService.findResourceAccessCount("month", day);
            for (ResourcesAccessCount m : monthCounts) {
                resourcesAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
            // 获取最近一年的日志登录数统计情况
            List<ResourcesAccessCount> yearCounts = resourceLogService.findResourceAccessCount("year", day);
            for (ResourcesAccessCount m : yearCounts) {
                resourcesAccessCountService.saveOrUpdate(m.getId(), new GsonBuilder().setPrettyPrinting().create().toJson(m));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
