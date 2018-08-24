package com.enlink.es.controllers;

import com.enlink.es.base.BaseAction;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.dto.NetworkTrafficDto;
import com.enlink.es.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.derivative.ParsedDerivative;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/network/")
public class NetworkController extends BaseAction {
    @Autowired
    private RestHighLevelClient esClient;
    private String field;

    @GetMapping("/traffic")
    public AjaxResults traffic() throws Exception {
        String today = DateUtils.date2string(new Date());
        SearchRequest request = new SearchRequest();
        request.indices("metricbeat-6.3.1-" + DateUtils.date2stringPoint(new Date()));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);

        String from = DateUtils.date2string(DateUtils.firstSecond(new Date()));
        String to = DateUtils.date2string(DateUtils.lastSecond(new Date()));

        String field = "@timestamp";
        AggregationBuilder aggs = AggregationBuilders.dateRange("dateRange").field(field)
                .format("yyyy-MM-dd HH:mm:ss").addUnboundedFrom(from).addUnboundedTo(to)
                .subAggregation(AggregationBuilders.dateHistogram("network_speed_per_day")
                        .field(field).interval(10)
                        .dateHistogramInterval(DateHistogramInterval.MINUTE)
                        .subAggregation(AggregationBuilders.max("network_in_bytes").field("system.network.in.bytes"))
                        .subAggregation(PipelineAggregatorBuilders.derivative("in_derivative", "network_in_bytes").unit("1s"))
                        .subAggregation(AggregationBuilders.max("network_out_bytes").field("system.network.out.bytes"))
                        .subAggregation(PipelineAggregatorBuilders.derivative("out_derivative", "network_out_bytes").unit("1s"))
                );
        sourceBuilder.aggregation(aggs);
        request.source(sourceBuilder);

        LOGGER.info("Elasticsearch Search Index: " + request.source().toString());

        SearchResponse response = esClient.search(request);

        List<NetworkTrafficDto> nts = new ArrayList<>();
        ParsedDateRange dateRange = response.getAggregations().get("dateRange");
        if (dateRange.getBuckets().size() > 0) {
            ParsedDateHistogram histogram = dateRange.getBuckets().get(0).getAggregations().get("network_speed_per_day");
            for (Histogram.Bucket bk : histogram.getBuckets()) {
                DateTime dt = (DateTime) bk.getKey();
                String key = DateUtils.datetime2string(dt.toDate());
                ParsedDerivative inDerivative = bk.getAggregations().get("in_derivative");
                double inBytes = 0;
                if (null != inDerivative) {
                    inBytes = inDerivative.normalizedValue();
                }
                ParsedDerivative outDerivative = bk.getAggregations().get("out_derivative");
                double outBytes = 0;
                if (null != outDerivative) {
                    outBytes = outDerivative.normalizedValue();
                }
                nts.add(new NetworkTrafficDto(key, inBytes, outBytes * -1));
            }
        }
        return Results.resultOf(ResultCode.OK, nts);
    }
}
