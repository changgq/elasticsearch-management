package com.enlink.es.tasks;

import com.enlink.es.models.LogSetting;
import com.enlink.es.services.IndexAliasesRepository;
import com.enlink.es.services.IndexRepository;
import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${elasticsearch.index.filebeat}")
    private String filebeatIndex;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private IndexAliasesRepository indexAliasesRepository;

    /**
     * 每日凌晨1点执行删除任务
     */
    @Async
    @Scheduled(cron = "0 */2 * * * ?") // 凌晨1点：0 0 0 * * ?
    public void run() {
        LOGGER.info("Daily delete Index task start......");
        try {
            // 查询logsetting信息
            LogSetting logSetting = new LogSetting();

            String lastDelete = logSetting.getLast_delete_date();
            Date startTime = DateUtils.string2date(lastDelete);
            Date endTime = DateUtils.minus(new Date(), (int) logSetting.getDay_rate());
            while (startTime.getTime() < endTime.getTime()) {
                deleteIndex(filebeatIndex, startTime);
                startTime = DateUtils.plus(startTime, 1);
            }
            logSetting.setLast_delete_date(DateUtils.date2string(endTime));
            // 更新logsetting信息

        } catch (Exception e) {
            LOGGER.error("定时删除任务执行失败，失败原因：" + e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("Daily delete Index task end !");
    }

    /**
     * 按天删除索引
     *
     * @param index
     * @param day
     */
    private void deleteIndex(String index, Date day) throws Exception {
        String[] aliasesName = {"user", "res"};
        for (String aliase : aliasesName) {
            indexAliasesRepository.delete(index, aliase + "-" + DateUtils.date2string(day));
            indexAliasesRepository.delete(index, aliase + "-" + DateUtils.date2monthstring(day));
            indexAliasesRepository.delete(index, aliase + "-" + DateUtils.date2yearstring(day));
        }
        indexRepository.close(index);
    }
}
