package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.UserAccessCount;
import com.enlink.es.models.UserLoginCount;
import com.enlink.es.services.UserAccessCountService;
import com.enlink.es.services.UserLoginCountService;
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
public class UserLoginCountServiceImpl extends GeneralAbstractServiceImpl<UserLoginCount> implements UserLoginCountService {

    @Value("${elasticsearch.index.userLogin}")
    private String userLoginIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(userLoginIndex)
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
                        "      \"username\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"user_group\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"full_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"access_count\": {\n" +
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
