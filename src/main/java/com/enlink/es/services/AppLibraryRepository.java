package com.enlink.es.services;

import com.enlink.es.models.AppLibrary;

/**
 * 应用库业务层接口
 *
 * @author changgq
 */
public interface AppLibraryRepository extends GeneralRepository<AppLibrary> {

    /**
     * 根据appUrl获取已配置的应用
     *
     * @param appUrl
     * @return
     * @throws Exception
     */
    AppLibrary findByAppUrls(String appUrl) throws Exception;

}
