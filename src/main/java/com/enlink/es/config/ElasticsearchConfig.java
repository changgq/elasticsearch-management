package com.enlink.es.config;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ElasticsearchConfig {
    @Value("${elasticsearch.hosts}")
    private String hosts;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Value("${elasticsearch.index.filebeat}")
    private String filebeatIndex;

    @Value("${elasticsearch.index.logSetting}")
    private String logSettingIndex;

    @Value("${elasticsearch.index.logDownload}")
    private String logDownloadIndex;

    @Value("${elasticsearch.index.appLibrary}")
    private String appLibraryIndex;

    @Value("${elasticsearch.index.topRanking}")
    private String topRankingIndex;

    @Value("${elasticsearch.query.maxTotal}")
    private String queryMaxTotal;

    @Value("${elasticsearch.count.cycles}")
    private String countCycles;

    @Value("${elasticsearch.aliases.prefixs}")
    private String aliasesPrefixs;

    public String[] genHosts() {
        if (Strings.isNotBlank(hosts)) {
            return hosts.split(",");
        }
        return null;
    }

    public String[] genCountCycles() {
        if (Strings.isNotBlank(countCycles)) {
            return countCycles.split(",");
        }
        return null;
    }

    public String[] genAliasesPrefixs() {
        if (Strings.isNotBlank(aliasesPrefixs)) {
            return aliasesPrefixs.split(",");
        }
        return null;
    }
}
