package com.enlink.es.services.impl;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.UserLog;
import com.enlink.es.services.UserLogService;
import com.enlink.es.utils.DateUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
        return new IndicesCreateInfo.IndicesCIBuilder(userLogIndex).create();
    }

    @Override
    public List<Map<String, Object>> getUserLoginCount(String type) throws Exception {
        return docCountByTimestamp(type, "doc['user_id'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value");
    }
}
