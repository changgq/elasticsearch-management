package com.enlink.es.services;

/**
 * 索引相关业务层接口
 *
 * @author changgq
 */
public interface IndexRepository {

    /**
     * 打开索引
     *
     * @param index
     * @throws Exception
     */
    void open(String index) throws Exception;

    /**
     * 关闭索引
     *
     * @param index
     * @throws Exception
     */
    void close(String index) throws Exception;

}
