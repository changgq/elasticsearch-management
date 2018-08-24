package com.enlink.es.services;

import java.util.List;

/**
 * 索引别名业务层接口
 *
 * @author changgq
 */
public interface IndexAliasesRepository {


    /**
     * 按过滤条件为索引添加别名
     *
     * @param index
     * @param alias
     * @param filter
     * @throws Exception
     */
    void add(String index, String alias, String filter) throws Exception;

    /**
     * 删除索引对应别名
     *
     * @param index
     * @param alias
     * @throws Exception
     */
    void delete(String index, String alias) throws Exception;

    /**
     * 判断索引别名是否存在
     *
     * @param index
     * @param alias
     * @throws Exception
     */
    boolean isExixts(String index, String alias) throws Exception;
}
