package com.enlink.es.services.impl;

import com.enlink.es.dto.ClusterInfoDto;
import com.enlink.es.services.MiscellaneousService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 混合api业务层接口
 *
 * @author changgq
 * @desc 主要实现Elasticsearch集群的相关接口
 */
@Slf4j
@Service
public class MiscellaneousServiceImpl implements MiscellaneousService {

    @Autowired
    private RestHighLevelClient elasticsearch;

    @Override
    public ClusterInfoDto info() throws Exception {
        MainResponse response = elasticsearch.info();
        ClusterInfoDto info = new ClusterInfoDto();
        info.setName(response.getNodeName());
        info.setClusterName(response.getClusterName().value());
        info.setClusterUuid(response.getClusterUuid());
        info.setVersion(response.getVersion().major + "." + response.getVersion().minor + "." + response.getVersion().revision);
        info.setCreateTime(response.getBuild().date());
        return info;
    }

    @Override
    public Boolean ping() throws Exception {
        return elasticsearch.ping();
    }
}
