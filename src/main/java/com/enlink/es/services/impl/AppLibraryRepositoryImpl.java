package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.AppLibrary;
import com.enlink.es.services.AppLibraryRepository;
import com.enlink.es.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 应用库业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class AppLibraryRepositoryImpl extends AbstractGeneralRepository<AppLibrary> implements AppLibraryRepository {

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
        return new IndicesCreateInfo.IndicesCIBuilder(esConfig.getAppLibraryIndex())
                .setMappings("{\n" +
                        "  \"doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"id\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"app_name\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"description\": {\n" +
                        "        \"type\": \"keyword\"\n" +
                        "      },\n" +
                        "      \"app_urls\": {\n" +
                        "        \"type\": \"keyword\"\n" +
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
    public PageInfo<AppLibrary> findByPaging(SearchCond searchCond) throws Exception {
        SearchResponse response = super.findByPagingCondition(searchCond);
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

        List<AppLibrary> dataList = new ArrayList<>();
        for (SearchHit sh : response.getHits().getHits()) {
            AppLibrary appLibrary = new AppLibrary();
            appLibrary = GsonUtils.reConvert2Object(sh.getSourceAsString(), AppLibrary.class);
            dataList.add(appLibrary);
        }
        pageInfo.setData(dataList);
        return pageInfo;
    }

    @Override
    public AppLibrary findByAppUrls(String appUrl) throws Exception {
        SearchRequest request = new SearchRequest(getIndicesCI().getName());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("app_urls", appUrl));
        request.source(sourceBuilder);

        LOGGER.info("Search Query string: " + sourceBuilder.toString());
        SearchResponse response = getClient().search(request);

        List<AppLibrary> appLibs = new ArrayList<>();
        for (SearchHit sh : response.getHits().getHits()) {
            AppLibrary al = (AppLibrary) GsonUtils.reConvert2List(sh.getSourceAsString(), AppLibrary.class);
            appLibs.add(al);
        }
        if (appLibs.size() > 1) {
            throw new Exception("URL: " + appUrl + " 配置了多个应用。");
        }
        return appLibs.size() > 0 ? appLibs.get(0) : null;
    }
}
