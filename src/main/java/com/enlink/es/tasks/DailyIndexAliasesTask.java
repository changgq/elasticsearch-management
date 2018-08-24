package com.enlink.es.tasks;

import com.enlink.es.base.CountCycle;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.services.IndexAliasesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每日定时创建索引别名任务
 *
 * @author changgq
 */
@Slf4j
@Component
public class DailyIndexAliasesTask {

    @Autowired
    private ElasticsearchConfig esConfig;

    @Autowired
    private IndexAliasesRepository indexAliasesRepository;

    /**
     * 每日凌晨0点开始执行
     *
     * @desc 凌晨0点：0 0 0 * * ?
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        LOGGER.info("Daily Create Index Aliase Task: start......");
        try {
            Date today = new Date();
            // 循环别名前缀
            for (String prefix : esConfig.genAliasesPrefixs()) {
                // 循环周期
                for (String cl : esConfig.genCountCycles()) {
                    CountCycle cy = CountCycle.getCountCycle("", cl, today);
                    String filters = "{\"range\" : " +
                            "   {\"create_at\" : " +
                            "       { \"gte\": \"" + cy.getFrom() + "\", " +
                            "         \"lte\": \"" + cy.getTo() + "\", " +
                            "         \"format\": \"" + cy.getPattern() + "\" " +
                            "       }" +
                            "   }" +
                            "}";
                    indexAliasesRepository.add(esConfig.getFilebeatIndex(), prefix + "-" + cy.getCycle(), filters);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Daily Create Index Aliase Task: Failure, The Reason: " + e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("Daily Create Index Aliase Task: end !");
    }
}
