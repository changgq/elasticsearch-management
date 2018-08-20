package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.UserLog;
import com.enlink.es.services.UserLogService;
import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserLogServiceImpl extends GeneralAbstractServiceImpl<UserLog> implements UserLogService {

    @Value("${elasticsearch.index.user}")
    private String userLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(userLogIndex)
                .setNumberOfShards(1)
                .setNumberOfReplicas(1)
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
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .create();
    }

    @Override
    public List<Map<String, Object>> getUserLoginCount(String type) throws Exception {
        String cycle = super.getCycle(type);
        SearchRequest request = new SearchRequest().indices(getIndicesCI().getName());
        SearchSourceBuilder sourceBuiilder = new SearchSourceBuilder().fetchSource(false);
        sourceBuiilder.query(QueryBuilders.rangeQuery("timestamp").format("yyyy-MM-dd")
                .from(DateUtils.getYestoday())
                .to(DateUtils.getYestoday()));
        request.source(sourceBuiilder);

        return null;
    }
}
