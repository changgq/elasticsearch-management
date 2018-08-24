package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.LogSetting;
import com.enlink.es.services.LogSettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogSettingRepositoryImpl extends AbstractGeneralRepository<LogSetting> implements LogSettingRepository {

    @Autowired
    private ElasticsearchConfig esConfig;

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public RestHighLevelClient getClient() {
        return esClient;
    }

    @Override
    public ElasticsearchConfig getEsConfig() {
        return esConfig;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() {
        return new IndicesCreateInfo.IndicesCIBuilder(esConfig.getLogSettingIndex())
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"id\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"use_third_db\": {\n" +
                        "        \"type\": \"boolean\"\n" +
                        "      },\n" +
                        "      \"config_type\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"start_save_time\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd\"\n" +
                        "      },\n" +
                        "      \"day_rate\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"count_rate\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"last_delete_date\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd\"\n" +
                        "      },\n" +
                        "      \"last_backups_date\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd\"\n" +
                        "      },\n" +
                        "      \"start_status\": {\n" +
                        "        \"type\": \"boolean\"\n" +
                        "      },\n" +
                        "      \"log_types\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"log_levels\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"create_at\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"strict_date_optional_time||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||dd/MMM/yyyy:HH:mm:ss Z||epoch_millis\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .create();
    }
}
