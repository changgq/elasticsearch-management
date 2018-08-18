package com.enlink.es.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动后初始化基础数据
 */
@Component
@Order(2)
@Slf4j
public class StartInitRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("init index start ......");

        LOGGER.info("init index success!");
    }
}
