package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.LogDownload;
import com.enlink.es.services.LogDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 日志下载业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class LogDownloadServiceImpl extends GeneralAbstractServiceImpl<LogDownload> implements LogDownloadService {

    @Value("elasticsearch.index.logDownload")
    private String logDownloadIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(logDownloadIndex)
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"id\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"file_name\": {\n" +
                        "        \"type\": \"boolean\"\n" +
                        "      },\n" +
                        "      \"file_size\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"percent\": {\n" +
                        "        \"type\": \"float\"\n" +
                        "      },\n" +
                        "      \"log_type\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"download_count\": {\n" +
                        "        \"type\": \"long\"\n" +
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
