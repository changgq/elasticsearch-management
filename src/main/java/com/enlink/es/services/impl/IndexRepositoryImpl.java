package com.enlink.es.services.impl;

import com.enlink.es.services.IndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 索引常用业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class IndexRepositoryImpl implements IndexRepository {

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public void open(String index) throws Exception {
        OpenIndexRequest request = new OpenIndexRequest(index);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.masterNodeTimeout("1m");
        OpenIndexResponse openIndexResponse = esClient.indices().open(request);
        boolean acknowledged = openIndexResponse.isAcknowledged();
        boolean shardsAcked = openIndexResponse.isShardsAcknowledged();
    }

    @Override
    public void close(String index) throws Exception {
        CloseIndexRequest request = new CloseIndexRequest("index");
        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.masterNodeTimeout("1m");
        CloseIndexResponse closeIndexResponse = esClient.indices().close(request);
        boolean acknowledged = closeIndexResponse.isAcknowledged();
    }
}
