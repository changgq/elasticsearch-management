package com.enlink.es;

import com.enlink.es.config.ElasticsearchConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动Application
 */
@EnableScheduling
@SpringBootApplication
@Slf4j
public class ApplicationRun {

    @Autowired
    private ElasticsearchConfig esConfig;

    /**
     * 加载Elasticsearch客户端到容器
     *
     * @return
     */
    @Bean
    public RestHighLevelClient esClient() {
        LOGGER.info("Init Elasticsearch Client: start ......");
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String host : esConfig.genHosts()) {
            httpHosts.add(HttpHost.create(host));
        }
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[esConfig.genHosts().length]));
        if (Strings.isNotBlank(esConfig.getUsername()) && Strings.isNotBlank(esConfig.getPassword())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esConfig.getUsername(), esConfig.getPassword()));
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.disableAuthCaching();
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        RestHighLevelClient client = new RestHighLevelClient(builder);
        LOGGER.info("Init Elasticsearch Client: success !");
        return client;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRun.class, args);
    }
}

