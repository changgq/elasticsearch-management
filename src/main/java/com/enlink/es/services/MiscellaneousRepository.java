package com.enlink.es.services;

import com.enlink.es.dto.ClusterInfoDto;

/**
 * 混合Service
 */
public interface MiscellaneousRepository {
    /**
     * 查询集群信息
     *
     * @return
     */
    public ClusterInfoDto info() throws Exception;

    /**
     * ping Elasticsearch
     *
     * @return
     */
    public Boolean ping() throws Exception;
}
