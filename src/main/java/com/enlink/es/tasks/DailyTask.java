package com.enlink.es.tasks;


import com.enlink.es.services.ResourcesAccessCountService;
import com.enlink.es.services.UserAccessCountService;
import com.enlink.es.services.UserLoginCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每日凌晨（0点）执行定时任务
 *
 * @author changgq
 */
@Slf4j
@Component
public class DailyTask {

    @Autowired
    private UserLoginCountService userLoginCountService;

    @Autowired
    private UserAccessCountService userAccessCountService;

    @Autowired
    private ResourcesAccessCountService resourcesAccessCountService;

    @Async
    @Scheduled(cron = "0 0 0 * * ?") // 凌晨1点：0 0 1 * * ?
    public void run() {

        userLoginDaily();
        userAccessDaily();
        resourcesAccessDaily();

    }


    /**
     * 按天统计用户登录次数
     */
    private void userLoginDaily() {
        // 获取最近一天的日志登录数统计情况

        // 获取最近一月的日志登录数统计情况

        // 获取最近一年的日志登录数统计情况
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
