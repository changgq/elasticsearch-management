package com.enlink.es.services.impl;

import com.enlink.es.services.IndexAliasesRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;

/**
 * 索引别名常用业务层实现
 *
 * @author changgq
 */
@Slf4j
@Service
public class IndexAliasesRepositoryImpl implements IndexAliasesRepository {

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public void add(String[] indices, String alias, String filter) throws Exception {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                        .indices(indices)
                        .alias(alias)
                        .filter(filter);
        request.addAliasAction(aliasAction);
        IndicesAliasesResponse indicesAliasesResponse = esClient.indices().updateAliases(request);
        boolean acknowledged = indicesAliasesResponse.isAcknowledged();
    }

    @Override
    public void delete(String index, String alias) throws Exception {
        if (isExixts(index, alias)) {
            IndicesAliasesRequest request = new IndicesAliasesRequest();
            IndicesAliasesRequest.AliasActions aliasAction =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                            .index(index)
                            .alias(alias);
            request.addAliasAction(aliasAction);
            IndicesAliasesResponse indicesAliasesResponse = esClient.indices().updateAliases(request);
            boolean acknowledged = indicesAliasesResponse.isAcknowledged();
        }
    }

    @Override
    public boolean isExixts(String index, String alias) throws Exception {
        GetAliasesRequest request = new GetAliasesRequest();
        request.indices(index);
        request.aliases(alias);
        return esClient.indices().existsAlias(request);
    }
}
