package com.enlink.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xpack.sql.jdbc.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

/**
 * 启动Application
 */
@EnableScheduling
@SpringBootApplication
@Slf4j
public class ApplicationRun {

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

//    @Bean
//    public Connection connection() throws SQLException {
//        JdbcDataSource dataSource = new JdbcDataSource();
//        String address = "jdbc:es://" + elasticsearchConn;
//        dataSource.setUrl(address);
//        Properties connectionProperties = new Properties();
//        dataSource.setProperties(connectionProperties);
//        Connection connection = dataSource.getConnection();
//        return connection;
//    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApplicationRun.class, args);

        // 使用RestHighLevelClient客户端
//        RestHighLevelClient client = (RestHighLevelClient) ctx.getBean("elasticsearch");
//        try {
//            MainResponse response = client.info();
//            System.out.println(response.getClusterName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // SQL需要购买xpack后方可使用
//        Connection conn = (Connection) ctx.getBean("connection");
//        try (Statement statement = conn.createStatement();
//             ResultSet results = statement.executeQuery("SELECT count(*) as count FROM res* ")) {
//            int total = results.getInt(1);
//            System.out.println(total);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

