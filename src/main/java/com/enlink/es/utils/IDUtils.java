package com.enlink.es.utils;

import java.util.UUID;

/**
 * ID编码生产器
 *
 * @author changgq
 */
public class IDUtils {
    /**
     * 生成32位Id
     *
     * @return
     */
    public static String genIdx32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 生成36位Id
     *
     * @return
     */
    public static String genIdx36() {
        return UUID.randomUUID().toString().toLowerCase();
    }
}
