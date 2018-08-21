package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.SystemLog;
import com.enlink.es.services.SystemLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 系统日志业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class SystemLogServiceImpl extends GeneralAbstractServiceImpl<SystemLog> implements SystemLogService {

    @Value("${elasticsearch.index.system}")
    private String systemLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(systemLogIndex).create();
    }
}
