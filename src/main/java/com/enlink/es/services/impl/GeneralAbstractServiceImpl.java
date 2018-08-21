package com.enlink.es.services.impl;

import com.enlink.es.base.Condt;
import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageData;
import com.enlink.es.models.GeneralModel;
import com.enlink.es.services.GeneralService;
import com.enlink.es.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class GeneralAbstractServiceImpl<T extends GeneralModel> implements GeneralService<T> {

    public abstract RestHighLevelClient getClient() throws Exception;

    public abstract IndicesCreateInfo getIndicesCI() throws Exception;

    @Override
    public boolean isExists() throws Exception {
        return getClient().indices().exists(new GetIndexRequest().indices(getIndicesCI().getName()));
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
        } else {
            LOGGER.info("The Index is Exists!");
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
    public void saveOrUpdate(String id, String sources) throws Exception {
        IndexRequest request = new IndexRequest(getIndicesCI().getName(), getIndicesCI().getType());
        if (Strings.isNotBlank(id)) {
            request.id(id);
        }
        request.source(sources, XContentType.JSON);
        IndexResponse indexResponse = getClient().index(request);
    }

    @Override
    public void delete(String id) throws Exception {
        DeleteRequest request = new DeleteRequest(getIndicesCI().getName(), getIndicesCI().getType(), id);
        DeleteResponse deleteResponse = getClient().delete(request);
    }

    @Override
    public Map<String, Object> findById(String id) throws Exception {
        Map<String, Object> sourceAsMap = new HashMap<>();
        GetRequest getRequest = new GetRequest(getIndicesCI().getName(), getIndicesCI().getType(), id);
        GetResponse getResponse = getClient().get(getRequest);
        if (getResponse.isExists()) {
            sourceAsMap = getResponse.getSourceAsMap();
        } else {
            LOGGER.warn("Index : " + getResponse.getIndex() + " type : " + getResponse.getType() + " id : " + getResponse.getId() + " is not exists!");
        }
        return sourceAsMap;
    }

    @Override
    public boolean documentIsExists(String id) throws Exception {
        return getClient().exists(new GetRequest().index(getIndicesCI().getName()).id(id));
    }

    @Override
    public List<T> findByCycleType(Class<T> cls, Map<String, Object> conditions, String count_type, int top) throws Exception {
        CyclePojo cycle = getCycle(count_type);
        SearchRequest request = new SearchRequest(getIndicesCI().getName());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("count_type", cycle.getName()));
        sourceBuilder.query(QueryBuilders.termQuery("cycle", cycle.getValue()));
        if (null != conditions) {
            for (String conditionKey : conditions.keySet()) {
                sourceBuilder.query(QueryBuilders.termsQuery(conditionKey, conditions.get(conditionKey)));
            }
        }
        sourceBuilder.size(top);
        sourceBuilder.sort("access_count", SortOrder.DESC);
        request.source(sourceBuilder);

        LOGGER.info("Search Query string: " + sourceBuilder.toString());
        SearchResponse response = getClient().search(request);

        List<T> datas = new ArrayList<>();
        for (SearchHit sh : response.getHits().getHits()) {
            T uac = (T) cls.newInstance().map2Object(sh.getSourceAsMap());
            datas.add(uac);
        }
        return datas;
    }

    @Override
    public PageData findByPaging(Condt condt, Integer pageIndex, Integer pageSize) throws Exception {
        // 根据条件查询所有数据，超过10000条的记录，按照分页查询。
        LOGGER.info("Indices Paging Search start ......");
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (null == condt) {
            sourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            if (null != condt.getTerms()) {
                for (String termKey : condt.getTerms().keySet()) {
                    sourceBuilder.query(QueryBuilders.termQuery(termKey, condt.getTerms().get(termKey)));
                }
            }
            if (null != condt.getFuzziness()) {
                for (String fuzzKey : condt.getFuzziness().keySet()) {
                    sourceBuilder.query(QueryBuilders.matchQuery(fuzzKey, condt.getFuzziness().get(fuzzKey))
                            .fuzziness(Fuzziness.AUTO)
                            .prefixLength(3)
                            .maxExpansions(10));
                }
            }
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        int startIndex = condt.getPageIndex() > 0 ? (condt.getPageIndex() - 1) * condt.getPageSize() : 0;
        // 如果下一页的最大值超过10000，则跳转到第1页
        if ((startIndex + condt.getPageSize()) > 10000) {
            startIndex = 0;
        }
        sourceBuilder.from(startIndex);
        sourceBuilder.size(condt.getPageSize());
        request.source(sourceBuilder);

        LOGGER.debug("Elasticsearch Query : " + request.toString());
        SearchResponse response = getClient().search(request);
        LOGGER.debug("Elasticsearch Query results : " + response.getHits());

        // 总记录超过10000的，则总是为10000，因为Elasticsearch分页查询只支持10000以内的。
        long total = 0L;
        long totalHits = response.getHits().totalHits;
        if (totalHits < 10000) {
            total = totalHits;
        }
        LOGGER.info("Indices Paging Search end!");

        PageData pageData = new PageData();
        pageData.setPageIndex(condt.getPageIndex());
        pageData.setPageSize(condt.getPageSize());
        pageData.setTotal(total);
        pageData.setData(response.getHits().getHits());

        return pageData;
    }


    /**
     * 根据timestamp和script按年、月、日（默认：昨日）统计doc数量
     *
     * @param type
     * @param scripts
     * @return
     * @throws Exception
     */
    protected CyclePojo docCountByTimestamp(String type, String scripts) throws Exception {
        List<Map<String, Object>> datas = new ArrayList<>();
        CyclePojo cycle = getCycle(type);

        SearchRequest request = new SearchRequest().indices(getIndicesCI().getName());

        SearchSourceBuilder sourceBuiilder = new SearchSourceBuilder().fetchSource(false);
        sourceBuiilder.query(
                QueryBuilders.rangeQuery("@timestamp")
                        .format(cycle.getPattern())
                        .from(cycle.getFrom())
                        .to(cycle.getTo()));

        sourceBuiilder.aggregation(
                AggregationBuilders.terms("distinct")
                        .size(Integer.MAX_VALUE)
                        .script(new Script(scripts)));
        request.source(sourceBuiilder);

        LOGGER.info("Elasticsearch search query string : " + request.source().toString());

        SearchResponse response = getClient().search(request);
        Terms terms = response.getAggregations().get("distinct");

        Map<String, Object> map = new HashMap<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            map.put(bucket.getKeyAsString(), bucket.getDocCount());
        }

        cycle.setDatas(map);
        return cycle;
    }

    /**
     * 根据类型获取周期对象
     *
     * @param count_type
     * @return
     */
    protected CyclePojo getCycle(String count_type) {
        Date yesterday = DateUtils.getYesterday();
        CyclePojo cyclePojo = new CyclePojo();
        cyclePojo.setName(count_type);
        cyclePojo.setPattern("yyyy-MM-dd");
        if ("month".equals(count_type)) {
            cyclePojo.setValue(DateUtils.date2monthstring(yesterday));
            cyclePojo.setFrom(DateUtils.date2string(DateUtils.firstDayOfMonth(yesterday)));
            cyclePojo.setTo(DateUtils.date2string(DateUtils.lastDayOfMonth(yesterday)));
        } else if ("year".equals(count_type)) {
            cyclePojo.setValue(DateUtils.date2yearstring(yesterday));
            cyclePojo.setFrom(DateUtils.date2string(DateUtils.firstDayOfYear(yesterday)));
            cyclePojo.setTo(DateUtils.date2string(DateUtils.lastDayOfYear(yesterday)));
        } else {
            cyclePojo.setValue(DateUtils.date2string(yesterday));
            cyclePojo.setFrom(DateUtils.date2string(yesterday));
            cyclePojo.setTo(DateUtils.date2string(yesterday));
        }
        return cyclePojo;
    }

    @Data
    @NoArgsConstructor
    class CyclePojo {
        private String name;
        private String value;
        private String pattern;
        private String from;
        private String to;
        private Map<String, Object> datas;
    }
}
