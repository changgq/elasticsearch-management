package com.enlink.es.controllers;

import com.enlink.es.base.PageInfo;
import com.enlink.es.controllers.requests.LogSearchRequest;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.models.UserLog;
import com.enlink.es.services.LogSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/search/")
public class LogSearchController {
    @Autowired
    private LogSearchRepository logSearchRepository;

    /**
     * 分页查询
     *
     * @return
     */
    @PostMapping("/{indexPrefix}/list")
    public AjaxResults list(@PathVariable String indexPrefix, @RequestBody LogSearchRequest request) throws Exception {
        if (Strings.isBlank(indexPrefix)) {
            return Results.errorOf(ResultCode.THE_REQUIRED_PARAMETERS_ARE_MISSING, null);
        }
        QueryBuilder qb = createQueryBuilder(request, indexPrefix);
        PageInfo<UserLog> pageInfo = logSearchRepository.findByPaging(request, indexPrefix, qb);
        return Results.resultOf(ResultCode.OK, pageInfo);
    }

    private QueryBuilder createQueryBuilder(LogSearchRequest request, String indexPrefix) {
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        String startTime_ = request.getDate() + " " + request.getStartTime();
        String endTime_ = request.getDate() + " " + request.getEndTime();
        booleanQuery.must(QueryBuilders.rangeQuery("create_at").format("yyyy-MM-dd HH:mm:ss").from(startTime_).to(endTime_));

        if (Strings.isNotBlank(request.getUserInfo())) {
            booleanQuery.should(QueryBuilders.wildcardQuery("full_name", "*" + request.getUserInfo() + "*"));
            booleanQuery.should(QueryBuilders.wildcardQuery("user_name", "*" + request.getUserInfo() + "*"));
            if ("admin".equals(indexPrefix)) {
                booleanQuery.should(QueryBuilders.wildcardQuery("role_name", "*" + request.getUserInfo() + "*"));
            } else {
                booleanQuery.should(QueryBuilders.wildcardQuery("user_group", "*" + request.getUserInfo() + "*"));
            }
        }
        if (Strings.isNotBlank(request.getAuthCenter())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("auth_center", "*" + request.getAuthCenter() + "*"));
        }
        if (Strings.isNotBlank(request.getRemoteIp())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("remote_ip", "*" + request.getRemoteIp() + "*"));
        }
        if (Strings.isNotBlank(request.getOperationMethod())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("operation_method", "*" + request.getOperationMethod() + "*"));
        }
        if (Strings.isNotBlank(request.getLogInfo())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("log_info", "*" + request.getLogInfo() + "*"));
        }
        if (Strings.isNotBlank(request.getRightName())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("right_name", "*" + request.getRightName() + "*"));
        }
        if (Strings.isNotBlank(request.getLogLevel())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("log_level", "*" + request.getLogLevel() + "*"));
        }
        if (Strings.isNotBlank(request.getAppType())) {
            booleanQuery.must(QueryBuilders.wildcardQuery("app_type", "*" + request.getAppType() + "*"));
        }
        return booleanQuery;
    }
}
