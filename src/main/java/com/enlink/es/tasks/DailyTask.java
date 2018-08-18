package com.enlink.es.tasks;


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


    @Async
    @Scheduled(cron = "0 0 0 * * ?") // 凌晨1点：0 0 1 * * ?
    public void run() {

    }


    /**
     * 按天统计用户登录次数
     */
    private void userLoginDaily() {

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
