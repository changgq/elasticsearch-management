package com.enlink.es.services;

import com.enlink.es.base.Condt;
import com.enlink.es.base.PageData;
import com.enlink.es.models.GeneralModel;
import com.enlink.es.models.UserLoginCount;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;

/**
 * Index操作相关通用业务接口
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
     * 保存或更新索引记录
     *
     * @param id
     * @param sources
     * @throws Exception
     */
    void saveOrUpdate(String id, String sources) throws Exception;

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
    Map<String, Object> findById(String id) throws Exception;

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
     * @param condt
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageData findByPaging(Condt condt, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 按周期类型查询用户访问数据top排行
     *
     * @param cls
     * @param count_type
     * @param top
     * @return
     * @throws Exception
     */
    List<T> findByCycleType(Class<T> cls, Map<String, Object> conditions, String count_type, int top) throws Exception;

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
