package com.enlink.es.config;

import com.enlink.es.models.LogSetting;
import com.enlink.es.services.LogSettingService;
import com.enlink.es.services.ResourcesAccessCountService;
import com.enlink.es.services.UserAccessCountService;
import com.enlink.es.services.UserLoginCountService;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 启动后初始化基础数据
 */
@Slf4j
@Order(2)
@Component
public class StartInitRunner implements CommandLineRunner {
    @Autowired
    private UserAccessCountService userAccessCountService;
    @Autowired
    private UserLoginCountService userLoginCountService;
    @Autowired
    private ResourcesAccessCountService resourcesAccessCountService;
    @Autowired
    private LogSettingService logSettingService;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("init index start ......");

        LOGGER.info("init Index{name = \"user-access-count\"} start ......");
        userAccessCountService.createIndex();
        LOGGER.info("init Index{name = \"user-access-count\"} success!");

        LOGGER.info("init Index{name = \"user-login-count\"} start ......");
        userLoginCountService.createIndex();
        LOGGER.info("init Index{name = \"user-login-count\"} success!");

        LOGGER.info("init Index{name = \"resource-access-count\"} start ......");
        resourcesAccessCountService.createIndex();
        LOGGER.info("init Index{name = \"resource-access-count\"} success!");

        // 下载文件生成记录
        LOGGER.info("init Index{name = \"resource-access-count\"} start ......");
        resourcesAccessCountService.createIndex();
        LOGGER.info("init Index{name = \"resource-access-count\"} success!");

        // 日志设置信息
        LOGGER.info("init Index{name = \".log-setting\"} start ......");
        logSettingService.createIndex();
        boolean documentIsExists = logSettingService.documentIsExists("1");
        if (documentIsExists) {
            logSettingService.saveOrUpdate("1", new GsonBuilder().create().toJson(new LogSetting().initial().toMap()));
        }
        LOGGER.info("init Index{name = \".log-setting\"} success!");

        LOGGER.info("init Index{name = \"user-filebeat\"} start ......");
//        userLoginCountService.createIndex();
        LOGGER.info("init Index{name = \"user-filebeat\"} success!");

        LOGGER.info("init Index{name = \"res-filebeat\"} start ......");
//        userLoginCountService.createIndex();
        LOGGER.info("init Index{name = \"res-filebeat\"} success!");

        LOGGER.info("init Index{name = \"system-filebeat\"} start ......");
//        userLoginCountService.createIndex();
        LOGGER.info("init Index{name = \"system-filebeat\"} success!");

        LOGGER.info("init Index{name = \"admin-filebeat\"} start ......");
//        userLoginCountService.createIndex();
        LOGGER.info("init Index{name = \"admin-filebeat\"} success!");

        LOGGER.info("init index success!");
    }
}
