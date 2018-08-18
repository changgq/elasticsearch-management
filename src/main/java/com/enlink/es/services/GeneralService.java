package com.enlink.es.services;

import com.enlink.es.base.Condt;
import com.enlink.es.base.PageData;
import com.enlink.es.models.GeneralModel;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;

/**
 * 通用业务接口
 *
 * @desc 每个继承该接口的索引业务接口将无需声明如下增删改查的索引接口。
 * @param <T>
 * @author changgq
 */
public interface GeneralService<T extends GeneralModel> {

    boolean isExists() throws Exception;

    void createIndex() throws Exception;

    default void createIndexSuccess(CreateIndexResponse createIndexResponse) {
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        if (acknowledged && shardsAcknowledged) {
            System.out.println("Index ：" + createIndexResponse.index() + " created !");
        }
    }

    default void createIndexFailure() {
        System.out.println("Index create failure!");
    }

    boolean putMappings() throws Exception;

    PageData<T> findByPaging(Condt condt, Integer pageIndex, Integer pageSize) throws Exception;
}
