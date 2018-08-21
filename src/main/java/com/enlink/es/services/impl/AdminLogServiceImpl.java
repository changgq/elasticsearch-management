package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.AdminLog;
import com.enlink.es.services.AdminLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 管理员日志业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class AdminLogServiceImpl extends GeneralAbstractServiceImpl<AdminLog> implements AdminLogService {
    @Value("${elasticsearch.index.admin}")
    private String adminLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(adminLogIndex).create();
    }
}
