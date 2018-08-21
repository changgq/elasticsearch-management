package com.enlink.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
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
    private String host;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    /**
     * 加载Elasticsearch客户端到容器
     *
     * @return
     */
    @Bean
    public RestHighLevelClient elasticsearch() {
        LOGGER.info("init elasticsearch client start ......");
        RestClientBuilder builder = RestClient.builder(HttpHost.create(host));
        // 校验 username 和 password
        if (Strings.isNotBlank(username) && Strings.isNotBlank(password)) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.disableAuthCaching();
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        RestHighLevelClient client = new RestHighLevelClient(builder);
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

