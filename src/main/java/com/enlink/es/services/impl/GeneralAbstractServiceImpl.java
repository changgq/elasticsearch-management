package com.enlink.es.services.impl;

import com.enlink.es.base.Condt;
import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageData;
import com.enlink.es.models.GeneralModel;
import com.enlink.es.services.GeneralService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@Slf4j
public abstract class GeneralAbstractServiceImpl<T extends GeneralModel> implements GeneralService<T> {

    public abstract RestHighLevelClient getClient() throws Exception;

    public abstract IndicesCreateInfo getIndicesCI() throws Exception;

    @Override
    public boolean isExists() throws Exception {
        return getClient().exists(new GetRequest(getIndicesCI().getName()));
    }

    @Override
    public void createIndex() throws Exception {
        // 判断索引是否存在
        if (!isExists()) {
            // 获取索引创建信息
            IndicesCreateInfo ci = getIndicesCI();
            // 创建索引
            CreateIndexRequest request = new CreateIndexRequest(ci.getName());
            // 通过json字符串创建索引
            if (Strings.isNotBlank(ci.getSource())) {
                request.source(ci.getSource(), XContentType.JSON);
            } else {
                request.settings(Settings.builder()
                        .put("index.number_of_shards", ci.getNumber_of_shards())
                        .put("index.number_of_replicas", ci.getNumber_of_replicas())
                );
                if (Strings.isNotBlank(ci.getMappings())) {
                    request.mapping(ci.getType(), ci.getMappings(), XContentType.JSON);
                }
                if (ci.getAliases().size() > 0) {
                    for (Alias alias : ci.getAliases()) {
                        request.alias(alias);
                    }
                }
                if (Strings.isNotBlank(ci.getTimeout())) {
                    request.timeout(ci.getTimeout());
                }
                if (Strings.isNotBlank(ci.getMasterNodeTimeout())) {
                    request.masterNodeTimeout(ci.getMasterNodeTimeout());
                }
                if (null != ci.getWaitForActiveShards()) {
                    request.waitForActiveShards(ci.getWaitForActiveShards());
                }
            }
            if (ci.isAsync()) {
                getClient().indices().createAsync(request, new ActionListener<CreateIndexResponse>() {
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        createIndexSuccess(createIndexResponse);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        createIndexFailure();
                    }
                });
            } else {
                getClient().indices().create(request);
            }
        }
    }


    @Override
    public boolean putMappings() throws Exception {
        // 判断索引是否存在
        if (!isExists()) {
            // 获取索引创建信息
            IndicesCreateInfo ci = getIndicesCI();
            // 创建索引
            PutMappingRequest request = new PutMappingRequest(ci.getName());
            request.type(ci.getType());
            // 通过json字符串创建索引
            if (Strings.isNotBlank(ci.getSource())) {
                request.source(ci.getSource(), XContentType.JSON);
            }
            if (Strings.isNotBlank(ci.getTimeout())) {
                request.timeout(ci.getTimeout());
            }
            if (Strings.isNotBlank(ci.getMasterNodeTimeout())) {
                request.masterNodeTimeout(ci.getMasterNodeTimeout());
            }
            if (ci.isAsync()) {
                getClient().indices().putMappingAsync(request, new ActionListener<PutMappingResponse>() {
                    @Override
                    public void onResponse(PutMappingResponse putMappingResponse) {
                        putMappingsSuccess(putMappingResponse);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        putMappingsFailure();
                    }
                });
            } else {
                getClient().indices().putMapping(request);
            }
        }
        return false;
    }

    @Override
    public PageData<T> findByPaging(Condt condt, Integer pageIndex, Integer pageSize) throws Exception {
        // 根据条件查询所有数据，超过10000条的记录，按照分页查询。
        LOGGER.info("Indices Paging Search start ......");
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        LOGGER.debug("Elasticsearch Query : " + searchRequest.toString());
        SearchResponse response = getClient().search(searchRequest);

        LOGGER.debug("Elasticsearch Query results : " + response.getHits());
        LOGGER.info("Indices Paging Search end!");
        return null;
    }
}
