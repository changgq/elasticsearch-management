package com.enlink.es.exceptions;

import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * @author changgq
 */
@Slf4j
@ControllerAdvice
public class GlobalException {

    @ResponseBody
    @ExceptionHandler
    public AjaxResults handler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        e.printStackTrace();
        return Results.errorOf(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
