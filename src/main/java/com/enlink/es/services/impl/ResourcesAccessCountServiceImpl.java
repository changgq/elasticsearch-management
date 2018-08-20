package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.ResourcesAccessCount;
import com.enlink.es.models.UserAccessCount;
import com.enlink.es.services.ResourcesAccessCountService;
import com.enlink.es.services.UserAccessCountService;
import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户访问统计业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class ResourcesAccessCountServiceImpl extends GeneralAbstractServiceImpl<ResourcesAccessCount> implements ResourcesAccessCountService {

    @Value("${elasticsearch.index.resourceAccess}")
    private String resourceAccessIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(resourceAccessIndex)
                .setNumberOfShards(1)
                .setNumberOfReplicas(0)
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"id\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"count_type\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"cycle\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"resource_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"domain_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"access_count\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"create_at\": {\n" +
                        "        \"type\": \"date\",\n" +
                        "        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .create();
    }
}