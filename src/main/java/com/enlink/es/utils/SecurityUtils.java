package com.enlink.es.utils;

import com.google.common.hash.Hashing;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import static com.google.common.base.Charsets.UTF_8;

/**
 * 加密工具类
 *
 * @author changgq
 * @desc 采用Guava中的hash算法
 */
public class SecurityUtils {
    private final static String MD5_KEY = "Enlink@123";

    /**
     * md5加密
     *
     * @param security
     * @return
     */
    public static String md5(String security) {
        String securityValue = "";
        try {
            Key md5Key = new SecretKeySpec(MD5_KEY.getBytes(UTF_8), "HmacMD5");
            securityValue = Hashing.hmacMd5(md5Key).hashBytes(security.getBytes()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return securityValue;
    }
}
