package com.enlink.es.services.impl;

import com.enlink.es.base.CountCycle;
import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.dto.TopRankingDto;
import com.enlink.es.models.TopRanking;
import com.enlink.es.services.TopRankingRepository;
import com.enlink.es.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LinkedMap;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregator;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行榜业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class TopRankingRepositoryImpl extends AbstractGeneralRepository<TopRanking> implements TopRankingRepository {

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
        return new IndicesCreateInfo.IndicesCIBuilder(getEsConfig().getTopRankingIndex())
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"id\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_topic\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_type\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_cycle\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_key\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_key_alias\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"rank_value\": {\n" +
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

    @Override
    public List<TopRankingDto> findByCountCycle(CountCycle countCycle, int top) throws Exception {
        SearchRequest request = new SearchRequest(getIndicesCI().getName());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        bool.must(QueryBuilders.termQuery("rank_topic", countCycle.getCountTopic()));
        bool.must(QueryBuilders.termQuery("rank_type", countCycle.getCountType()));
        bool.must(QueryBuilders.termQuery("rank_cycle", countCycle.getCycle()));
        sourceBuilder.query(bool);
        sourceBuilder.size(0);

        sourceBuilder.aggregation(AggregationBuilders.terms("distinct").field("rank_key_alias")
                .size(top)
                .subAggregation(AggregationBuilders.sum("accessCount").field("rank_value"))
                .order(BucketOrder.aggregation("accessCount", false)));

        request.source(sourceBuilder);

        LOGGER.info("Search Query string: " + sourceBuilder.toString());
        SearchResponse response = getClient().search(request);

        Terms terms = response.getAggregations().get("distinct");
        List<TopRankingDto> dtos = new ArrayList<>();
        for (Terms.Bucket bk : terms.getBuckets()) {
            ParsedSum sum = bk.getAggregations().get("accessCount");
            dtos.add(new TopRankingDto(bk.getKeyAsString(), (long) sum.getValue()));
        }
        return dtos;
    }
}
