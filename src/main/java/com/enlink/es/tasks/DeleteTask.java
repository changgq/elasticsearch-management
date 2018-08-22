package com.enlink.es.tasks;

import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定期删除任务
 *
 * @author changgq
 */
@Slf4j
@Component
public class DeleteTask {
    /**
     * 每日凌晨1点执行删除任务
     */
    @Async
    @Scheduled(cron = "0 */2 * * * ?") // 凌晨1点：0 0 0 * * ?
    public void run() {
        LOGGER.info("Daily delete Index task start......");

        LOGGER.info("Daily delete Index task end !");
    }
}
