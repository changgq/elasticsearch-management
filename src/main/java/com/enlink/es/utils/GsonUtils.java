package com.enlink.es.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json解析工具类
 * Created by guking on 2016/8/15.
 */
public class GsonUtils {
    private static Gson gson = null;

    static {
        if (null == gson) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    // setDateFormat：设置输出时间格式
//                    .disableHtmlEscaping()
                    // setDateFormat：避免Gson使用时将一些字符自动转换为Unicode转义字符
                    // 例如：{"s":"\u003c"}，显示为：{"s":"<"}
                    // .excludeFieldsWithoutExposeAnnotation()
                    // excludeFieldsWithoutExposeAnnotation：只导出使用了@Expose注释过的属性,如果没有设置这句，只是在类中相应属性进行设置，是无效的。
                    .enableComplexMapKeySerialization()
                    // enableComplexMapKeySerialization：支持Map的key为复杂对象的形式
                    // 当Map中数据类型为自定义类型时，需要开启本方法。
                    .serializeNulls()
                    // serializeNulls：把null值也转换，默认是不转换null值的，可以选择也转换
                    // .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    // setFieldNamingPolicy：字段(即Key值)首字母大写
//                    .setPrettyPrinting()
                    // setPrettyPrinting：对JSON结果格式化，比如添加换行
                    // .setVersion(1.0)
                    // setVersion：有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                    // @Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                    // @Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
                    .create();
        }
    }

    /**
     * Gson解析：object2json
     *
     * @param obj
     * @return
     */
    public static String convert(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Gsons解析：json2map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> reConvert2Map(String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(json, Map.class);
        return map;
    }

    /**
     * Gson解析：json2object
     *
     * @param json
     * @param objClass
     * @param <T>
     * @return
     */
    public static <T> T reConvert2Object(String json, Class<T> objClass) {
        T t = null;
        try {
            t = gson.fromJson(json, objClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Gson解析：json2List
     *
     * @param json
     * @return
     */
    public static List<?> reConvert2List(String json) {
        List<?> list = new ArrayList<>();
        try {
            list = gson.fromJson(json, new TypeToken<List<?>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gson解析：Json2List
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> List<T> reConvert2List(String json, Type type) {
        List<T> list = new ArrayList<>();
        try {
            list = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gson解析：Json2ListKeyMap
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> reConvert2ListKeyMaps(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = gson.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
