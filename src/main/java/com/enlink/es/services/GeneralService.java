package com.enlink.es.services;

import com.enlink.es.base.Condt;
import com.enlink.es.base.PageData;
import com.enlink.es.models.GeneralModel;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;

/**
 * 通用业务接口
 *
 * @param <T>
 * @author changgq
 * @desc 每个继承该接口的索引业务接口将无需声明如下增删改查的索引接口。
 */
public interface GeneralService<T extends GeneralModel> {

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
     * 条件分页查询
     *
     * @param condt
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageData<T> findByPaging(Condt condt, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 索引创建成功默认回调函数
     *
     * @param createIndexResponse
     */
    default void createIndexSuccess(CreateIndexResponse createIndexResponse) {
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        if (acknowledged && shardsAcknowledged) {
            System.out.println("Index ：" + createIndexResponse.index() + " created !");
        }
    }

    /**
     * 索引创建失败默认回调函数
     */
    default void createIndexFailure() {
        System.out.println("Index create failure!");
    }

    /**
     * 索引更新mapping成功默认回调函数
     *
     * @param putMappingResponse
     */
    default void putMappingsSuccess(PutMappingResponse putMappingResponse) {
        boolean acknowledged = putMappingResponse.isAcknowledged();
        if (acknowledged) {
            System.out.println("Index putMapping success !");
        }
    }

    /**
     * 索引更新mapping失败默认回调函数
     */
    default void putMappingsFailure() {
        System.out.println("Index putMapping failure!");
    }
}
