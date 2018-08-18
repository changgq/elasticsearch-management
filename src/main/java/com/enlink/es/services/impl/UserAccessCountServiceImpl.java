package com.enlink.es.services.impl;

import com.enlink.es.models.UserAccessCount;
import com.enlink.es.services.UserAccessCountService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户访问统计业务层实现
 *
 * @author changgq
 */
@Service
public class UserAccessCountServiceImpl implements UserAccessCountService {
    @Value("${elasticsearch.index.userAccess}")
    private String userAccessIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public void createIndex() throws Exception {
        // 判断索引是否存在
        boolean isExists = elasticsearch.exists(new GetRequest(userAccessIndex));
        if (!isExists) {
            // 创建索引
            elasticsearch.index(new IndexRequest(userAccessIndex).create(true));
        }
    }

    @Override
    public List<UserAccessCount> findByCycleType(String type, int top) throws Exception {
        // 判断索引是否存在
        boolean isExists = elasticsearch.exists(new GetRequest(userAccessIndex));

        return null;
    }
}
