package com.enlink.es.services;

import com.enlink.es.base.PageInfo;
import com.enlink.es.controllers.requests.LogSearchRequest;
import com.enlink.es.models.*;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * 日志查询业务层接口
 *
 * @author changgq
 */
public interface LogSearchRepository<T extends GeneralModel> {
    /**
     * 分页查询日志
     *
     * @param request
     * @return
     * @throws Exception
     */
    PageInfo<T> findByPaging(LogSearchRequest request, String indexPrefix, QueryBuilder queryBuilder) throws Exception;
}
