package com.enlink.es.controllers.responses;

/**
 * 相应结果处理
 *
 * @author changgq
 */
public class Results {

    public static AjaxResults resultOf(ResultCode resultCode, Object data) {
        return new AjaxResults(resultCode.getCode(), resultCode.getDescription(), data);
    }

    public static AjaxResults errorOf(ResultCode resultCode, String errorMsg) {
        return new AjaxResults(resultCode.getCode(), "[" + resultCode.getDescription() + "]:" + errorMsg, null);
    }
}
