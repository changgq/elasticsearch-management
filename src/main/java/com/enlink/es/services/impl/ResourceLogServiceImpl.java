package com.enlink.es.services.impl;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.models.ResourceLog;
import com.enlink.es.services.ResourceLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 资源日志业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class ResourceLogServiceImpl extends GeneralAbstractServiceImpl<ResourceLog> implements ResourceLogService {

    @Value("${elasticsearch.index.resources}")
    private String resourceLogIndex;

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public RestHighLevelClient getClient() throws Exception {
        return elasticsearch;
    }

    @Override
    public IndicesCreateInfo getIndicesCI() throws Exception {
        return new IndicesCreateInfo.IndicesCIBuilder(resourceLogIndex).create();
    }

    @Override
    public List<Map<String, Object>> findResourceAccessCount(String type) throws Exception {
        return docCountByTimestamp(type, "doc['resource_name'].value");
    }

    @Override
    public List<Map<String, Object>> findUserAccessCount(String type) throws Exception {
        return docCountByTimestamp(type, "doc['user_id'].value + '|' + doc['user_name'].value + '|' + doc['user_group'].value");
    }
}
