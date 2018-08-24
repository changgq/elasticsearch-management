package com.enlink.es.tasks;

import com.enlink.es.base.CountCycle;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.LogSetting;
import com.enlink.es.services.IndexAliasesRepository;
import com.enlink.es.services.IndexRepository;
import com.enlink.es.services.LogSettingRepository;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ElasticsearchConfig esConfig;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private IndexAliasesRepository indexAliasesRepository;

    @Autowired
    private LogSettingRepository logSettingRepository;

    /**
     * 每日凌晨1点执行删除任务
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        LOGGER.info("Daily Delete Index Task: start......");
        try {
            // Get LogSetting info by id = 1.
            if (logSettingRepository.documentIsExists("1")) {
                LogSetting logSetting = GsonUtils.reConvert2Object(logSettingRepository.findById("1"), LogSetting.class);
                String lastDelete = logSetting.getLast_delete_date();
                Date startTime = DateUtils.string2date(lastDelete);
                Date endTime = DateUtils.minus(new Date(), (int) logSetting.getDay_rate());
                while (startTime.getTime() < endTime.getTime()) {
                    deleteIndex(esConfig.getFilebeatIndex(), startTime);
                    startTime = DateUtils.plus(startTime, 1);
                }
                logSetting.setLast_delete_date(DateUtils.date2string(endTime));
                // Update LogSetting Info.
                logSettingRepository.saveOrUpdate(logSetting);
            }
        } catch (Exception e) {
            LOGGER.error("Daily Delete Index Task: failure, The Reason: " + e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("Daily Delete Index Task: end !");
    }

    /**
     * 按天删除索引
     *
     * @param index
     * @param day
     */
    private void deleteIndex(String index, Date day) throws Exception {
        // 循环所有别名前缀
        for (String prefix : esConfig.genAliasesPrefixs()) {
            // 循环所有统计周期
            for (String cl : esConfig.genCountCycles()) {
                CountCycle cy = CountCycle.getCountCycle("", cl, day);
                indexAliasesRepository.delete(index, prefix + "-" + cy.getCycle());
            }
        }
        // 关闭索引
        indexRepository.close(index);
    }
}
