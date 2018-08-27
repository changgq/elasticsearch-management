package com.enlink.es.services.impl;

import com.enlink.es.base.PageInfo;
import com.enlink.es.controllers.requests.LogSearchRequest;
import com.enlink.es.models.*;
import com.enlink.es.services.LogSearchRepository;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志查询业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class LogSearchRepositoryImpl<T extends GeneralModel> implements LogSearchRepository<T> {

    @Autowired
    private RestHighLevelClient esClient;


    @Override
    public PageInfo<T> findByPaging(LogSearchRequest request, String indexPrefix, QueryBuilder queryBuilder) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexPrefix + "-" + request.getDate());

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        int start_ = (request.getPageIndex() - 1) * request.getPageSize();
        sourceBuilder.from(start_);
        sourceBuilder.size(request.getPageSize());
        searchRequest.source(sourceBuilder);

        LOGGER.info("Elasticsearch Query String: " + searchRequest.source().toString());
        SearchResponse response = esClient.search(searchRequest);

        PageInfo<T> pageInfo = new PageInfo<>();
        long total = response.getHits().getTotalHits();
        if (total > 10000) {
            total = 10000;
        }
        pageInfo.setTotal(total);
        pageInfo.setPageIndex(request.getPageIndex());
        pageInfo.setPageSize(request.getPageSize());

        List<T> userLogList = new ArrayList<>();
        if (response.getHits().getHits().length > 0) {
            for (SearchHit sh : response.getHits().getHits()) {
                userLogList.add(new GsonBuilder().create().fromJson(sh.getSourceAsString(), new TypeToken<T>() {
                }.getType()));
            }
        }
        pageInfo.setData(userLogList);
        return pageInfo;
    }
}
