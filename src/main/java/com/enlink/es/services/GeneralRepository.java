package com.enlink.es.services;

import com.enlink.es.base.IndicesCreateInfo;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.config.ElasticsearchConfig;
import com.enlink.es.models.GeneralModel;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Index操作相关通用业务接口
 *
 * @param <T>
 * @author changgq
 * @desc 每个继承该接口的索引业务接口将无需声明如下增删改查的索引接口。
 */
public interface GeneralRepository<T extends GeneralModel> {

    /**
     * 获取Elasticsearch客户端
     *
     * @return
     * @throws Exception
     */
    RestHighLevelClient getClient();

    /**
     * 获取Elasticsearch配置信息
     *
     * @return
     * @throws Exception
     */
    ElasticsearchConfig getEsConfig();

    /**
     * 获取索引基本信息
     *
     * @return
     * @throws Exception
     */
    IndicesCreateInfo getIndicesCI();

    /**
     * 判断索引是否存在
     *
     * @return
     * @throws Exception
     */
    boolean isExists() throws Exception;

    /**
     * 创建索引
     *
     * @throws Exception
     */
    void createIndex() throws Exception;

    /**
     * 为索引添加Mapping关系
     *
     * @return
     * @throws Exception
     */
    boolean putMappings() throws Exception;

    /**
     * 保存或更新索引记录
     *
     * @param t
     * @throws Exception
     */
    void saveOrUpdate(T t) throws Exception;

    /**
     * 根据id删除索引记录
     *
     * @param id
     * @throws Exception
     */
    void delete(String id) throws Exception;

    /**
     * 根据id查询索引记录
     *
     * @param id
     * @return
     * @throws Exception
     */
    String findById(String id) throws Exception;

    /**
     * 根据索引判断索引是否存在
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean documentIsExists(String id) throws Exception;

    /**
     * 条件分页查询
     *
     * @param searchCond
     * @return
     * @throws Exception
     */
    PageInfo<T> findByPaging(SearchCond searchCond) throws Exception;


}
