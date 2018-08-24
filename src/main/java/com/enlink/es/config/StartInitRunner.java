package com.enlink.es.config;

import com.enlink.es.models.LogSetting;
import com.enlink.es.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动后初始化基础数据
 */
@Slf4j
@Order(5)
@Component
public class StartInitRunner implements CommandLineRunner {
    @Autowired
    private TopRankingRepository topRankingRepository;
    @Autowired
    private AppLibraryRepository appLibraryRepository;
    @Autowired
    private LogSettingRepository logSettingRepository;
    @Autowired
    private LogDownloadRepository logDownloadRepository;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Init Index: start ......");
        appLibraryRepository.createIndex();
        topRankingRepository.createIndex();
        logDownloadRepository.createIndex();

        logSettingRepository.createIndex();
        boolean documentIsExists = logSettingRepository.documentIsExists("1");
        if (!documentIsExists) {
            logSettingRepository.saveOrUpdate(new LogSetting().initial());
        }
        LOGGER.info("Init Index: success!");
    }
}
