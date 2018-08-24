package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.models.GeneralModel;
import com.enlink.es.services.GeneralRepository;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.GsonUtils;
import com.enlink.es.utils.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractGeneralRepository<T extends GeneralModel> implements GeneralRepository<T> {

    @Override
    public boolean isExists() throws Exception {
        return getClient().indices().exists(new GetIndexRequest().indices(getIndicesCI().getName()));
    }

    @Override
    public void createIndex() throws Exception {
        // 判断索引是否存在
        LOGGER.info("Create Index: start......");
        LOGGER.info("Create Index: indexName = " + getIndicesCI().getName());
        if (!isExists()) {
            LOGGER.info("Create Index: test 2 ......");
            // 获取索引创建信息
            IndicesCreateInfo ci = getIndicesCI();
            LOGGER.info("Create Index: test 3 ......");
            // 创建索引
            CreateIndexRequest request = new CreateIndexRequest(ci.getName());
            LOGGER.info("Create Index: test 4 ......");
            // 通过json字符串创建索引
            if (Strings.isNotBlank(ci.getSource())) {
                LOGGER.info("Create Index: test 5 ......");
                request.source(ci.getSource(), XContentType.JSON);
            } else {
                LOGGER.info("Create Index: test 6 ......");
                request.settings(Settings.builder()
                        .put("index.number_of_shards", ci.getNumber_of_shards())
                        .put("index.number_of_replicas", ci.getNumber_of_replicas())
                );
                if (Strings.isNotBlank(ci.getMappings())) {
                    System.out.println(ci.getMappings());
                    request.mapping(ci.getType(), ci.getMappings(), XContentType.JSON);
                }
                if (null != ci.getAliases() && ci.getAliases().size() > 0) {
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
            LOGGER.info("Create Index: test 7 ......");
            getClient().indices().create(request);

            LOGGER.info("Create Index: end !");
        } else {
            LOGGER.warn("Create Index: The Index is Exists!");
        }
    }


    @Override
    public boolean putMappings() throws Exception {
        LOGGER.info("PutMapping: Index == " + getIndicesCI().getName() + ", start......");
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
            PutMappingResponse response = getClient().indices().putMapping(request);
            return response.isAcknowledged() && response.isFragment();
        }
        LOGGER.info("PutMapping: end!");
        return false;
    }

    @Override
    public void saveOrUpdate(T t) throws Exception {
        LOGGER.info("Index Save or Update: Index == " + getIndicesCI().getName() + ", start......");
        IndexRequest request = new IndexRequest(getIndicesCI().getName(), getIndicesCI().getType());
        if (Strings.isBlank(t.getId())) {
            t.setId(IDUtils.genIdx32());
            t.setCreate_at(DateUtils.datetime2string(new Date()));
        }
        request.id(t.getId());
        request.source(GsonUtils.convert(t), XContentType.JSON);
        getClient().index(request);
        LOGGER.info("Index Save or Update: end!");
    }

    @Override
    public void delete(String id) throws Exception {
        LOGGER.info("Index Delete: Index == " + getIndicesCI().getName() + ", start......");
        DeleteRequest request = new DeleteRequest(getIndicesCI().getName(), getIndicesCI().getType(), id);
        getClient().delete(request);
        LOGGER.info("Index Delete: end!");
    }

    @Override
    public String findById(String id) throws Exception {
        LOGGER.info("Index findById: Index == " + getIndicesCI().getName() + ", start......");
        GetRequest getRequest = new GetRequest(getIndicesCI().getName(), getIndicesCI().getType(), id);
        GetResponse getResponse = getClient().get(getRequest);
        if (getResponse.isExists()) {
            return getResponse.getSourceAsString();
        } else {
            LOGGER.warn("Index : " + getResponse.getIndex() + " type : " + getResponse.getType() + " id : " + getResponse.getId() + " is not exists!");
        }
        LOGGER.info("Index findById: end!");
        return "";
    }

    @Override
    public boolean documentIsExists(String id) throws Exception {
        return getClient().exists(new GetRequest().index(getIndicesCI().getName()).id(id));
    }


    /**
     * 创建查询条件
     *
     * @param terms
     * @param fuzziness
     * @return
     */
    public QueryBuilder queryBuild(Map<String, Object> terms, Map<String, Object> fuzziness) {
        // 如果条件为空则返回匹配所有行
        if (null == terms && null == fuzziness) {
            return QueryBuilders.matchAllQuery();
        }
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        if (null != terms) {
            for (String termKey : terms.keySet()) {
                booleanQuery.must(QueryBuilders.termQuery(termKey, terms.get(termKey)));
            }
        }
        if (null != fuzziness) {
            for (String fuzzKey : fuzziness.keySet()) {
                booleanQuery.must(QueryBuilders.matchQuery(fuzzKey, fuzziness.get(fuzzKey)).fuzziness(Fuzziness.AUTO));
            }
        }
        return booleanQuery;
    }


    @Override
    public PageInfo findByPaging(SearchCond searchCond) throws Exception {
        // 根据条件查询所有数据，超过10000条的记录，按照分页查询。
        LOGGER.info("Indices Paging Search start ......");
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuild(searchCond.getTerms(), searchCond.getFuzziness()));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        int startIndex = searchCond.getPageIndex() > 0 ? (searchCond.getPageIndex() - 1) * searchCond.getPageSize() : 0;
        // 如果下一页的最大值超过10000，则跳转到第1页
        if ((startIndex + searchCond.getPageSize()) > 10000) {
            startIndex = 0;
        }
        sourceBuilder.from(startIndex);
        sourceBuilder.size(searchCond.getPageSize());
        request.source(sourceBuilder);

        LOGGER.debug("Elasticsearch Query : " + request.toString());
        SearchResponse response = getClient().search(request);
        LOGGER.debug("Elasticsearch Query results : " + response.getHits());

        // 总记录超过10000的，则总是为10000，因为Elasticsearch分页查询只支持10000以内的。
        long total = 0L;
        long totalHits = response.getHits().totalHits;
        if (totalHits < Long.valueOf(getEsConfig().getQueryMaxTotal())) {
            total = totalHits;
        }
        LOGGER.info("Indices Paging Search end!");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageIndex(searchCond.getPageIndex());
        pageInfo.setPageSize(searchCond.getPageSize());
        pageInfo.setTotal(total);
        pageInfo.setData(response.getHits().getHits());

        return pageInfo;
    }
}
