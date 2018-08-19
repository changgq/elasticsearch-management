package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
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
public class UserAccessCountServiceImpl extends GeneralAbstractServiceImpl<UserAccessCount> implements UserAccessCountService {

    @Value("${elasticsearch.index.userAccess}")
    private String userAccessIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(userAccessIndex).create();
    }

    @Override
    public List<UserAccessCount> findByCycleType(String type, int top) throws Exception {
        return null;
    }
}
