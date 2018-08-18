package com.enlink.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动Application
 */
@EnableScheduling
@SpringBootApplication
@Slf4j
public class ApplicationRun {

    // Elasticsearch连接hosts
    @Value("${elasticsearch.host}")
    private String elasticsearchConn;

    /**
     * 加载Elasticsearch客户端到容器
     *
     * @return
     */
    @Bean
    public RestHighLevelClient elasticsearch() {
        LOGGER.info("init elasticsearch client start ......");
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(HttpHost.create(elasticsearchConn)));
        LOGGER.info("init elasticsearch client success !");
        return client;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApplicationRun.class, args);
        RestHighLevelClient client = (RestHighLevelClient) ctx.getBean("elasticsearch");
        try {
            MainResponse response = client.info();
            System.out.println(response.getClusterName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

