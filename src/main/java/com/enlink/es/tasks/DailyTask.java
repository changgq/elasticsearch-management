package com.enlink.es.tasks;


import com.enlink.es.base.CountCycle;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.AppLibrary;
import com.enlink.es.models.TopRanking;
import com.enlink.es.services.*;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 每日凌晨（0点）执行定时任务
 *
 * @author changgq
 */
@Slf4j
@Component
public class DailyTask {
    @Autowired
    private ElasticsearchConfig esConfig;

    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    private TopRankingRepository topRankingRepository;

    @Autowired
    private AppLibraryRepository appLibraryRepository;

    /**
     * 每日凌晨0点开始执行
     *
     * @desc 凌晨0点：0 0 0 * * ?
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        LOGGER.info("Daily Count Task: start......");
        userLoginDaily(DateUtils.getYesterday());
        userAccessDaily(DateUtils.getYesterday());
        resourcesAccessDaily(DateUtils.getYesterday());
        LOGGER.info("Daily Count Task: end !");
    }


    /**
     * 按天统计用户登录次数
     */
    private void userLoginDaily(Date day) {
        try {
            String topic = "user-login";
            String scripts = "doc['full_name'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value";
            for (String cl : esConfig.genCountCycles()) {
                CountCycle dayCycle = CountCycle.getCountCycle(topic, cl, day);
                countByCreateAt(dayCycle, "user-" + dayCycle.getCycle(), scripts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 按天统计用户访问量
     */
    private void userAccessDaily(Date day) {
        try {
            String topic = "user-access";
            String scripts = "doc['full_name'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value";
            for (String cl : esConfig.genCountCycles()) {
                CountCycle dayCycle = CountCycle.getCountCycle(topic, cl, day);
                countByCreateAt(dayCycle, "res-" + dayCycle.getCycle(), scripts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按天统计资源访问量
     */
    private void resourcesAccessDaily(Date day) {
        try {
            String topic = "res-access";
            String scripts = "doc['resource_name'].value";
            for (String cl : esConfig.genCountCycles()) {
                CountCycle dayCycle = CountCycle.getCountCycle(topic, cl, day);
                countByCreateAt(dayCycle, "res-" + dayCycle.getCycle(), scripts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成排行榜key
     *
     * @param key
     * @return
     */
    private String genRankKey(String key) {
        StringBuffer sb = new StringBuffer();
        if (Strings.isNotBlank(key) && !key.replaceAll("\\|", "").equals("")) {
            for (String k : key.split("\\|")) {
                if (Strings.isNotBlank(k)) {
                    sb.append(k);
                } else {
                    sb.append("--");
                }
                sb.append(" / ");
            }
            if (sb.length() > 3) {
                sb.delete(sb.length() - 3, sb.length());
            }
        }
        return sb.toString();
    }

    /**
     * 根据createAt和scripts分别按时间格式（年、月、日）统计索引数量
     *
     * @param countCycle
     * @param index
     * @param scripts
     * @return
     * @throws Exception
     */
    private void countByCreateAt(CountCycle countCycle, String index, String scripts) throws Exception {
        SearchRequest request = new SearchRequest().indices(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().fetchSource(false);
        sourceBuilder.query(QueryBuilders.rangeQuery("create_at")
                .format(countCycle.getPattern())
                .from(countCycle.getFrom())
                .to(countCycle.getTo()));

        sourceBuilder.aggregation(
                AggregationBuilders.terms("distinct")
                        .size(Integer.MAX_VALUE)
                        .script(new Script(scripts)));
        request.source(sourceBuilder);

        LOGGER.info("Elasticsearch search query string : " + request.source().toString());

        SearchResponse response = esClient.search(request);
        Terms terms = response.getAggregations().get("distinct");

        for (Terms.Bucket bucket : terms.getBuckets()) {
            String rankKey = genRankKey(bucket.getKeyAsString());
            if (Strings.isNotBlank(rankKey)) {
                TopRanking tp = new TopRanking();
                tp.setRank_topic(countCycle.getCountTopic());
                tp.setRank_type(countCycle.getCountType());
                tp.setRank_cycle(countCycle.getCycle());
                tp.setRank_key(rankKey);
                tp.setRank_key_alias(rankKey);
                AppLibrary appLib = appLibraryRepository.findByAppUrls(rankKey);
                if (null != appLib) {
                    tp.setRank_key_alias(appLib.getApp_name());
                }
                tp.setRank_value(bucket.getDocCount());
                tp.setId(SecurityUtils.md5(tp.getRank_topic() + tp.getRank_type() + tp.getRank_cycle() + tp.getRank_key()));
                tp.setCreate_at(DateUtils.datetime2string(new Date()));
                topRankingRepository.saveOrUpdate(tp);
            }
        }
    }
}
