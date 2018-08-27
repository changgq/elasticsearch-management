package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.LogDownload;
import com.enlink.es.models.LogSetting;
import com.enlink.es.services.LogSettingRepository;
import com.enlink.es.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LogSettingRepositoryImpl extends AbstractGeneralRepository<LogSetting> implements LogSettingRepository {

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
        return new IndicesCreateInfo.IndicesCIBuilder(esConfig.getLogSettingIndex())
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
    public PageInfo<LogSetting> findByPaging(SearchCond searchCond) throws Exception {
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

        List<LogSetting> dataList = new ArrayList<>();
        for (SearchHit sh : response.getHits().getHits()) {
            LogSetting ls = new LogSetting();
            ls = GsonUtils.reConvert2Object(sh.getSourceAsString(), LogSetting.class);
            dataList.add(ls);
        }
        pageInfo.setData(dataList);
        return pageInfo;
    }
}
