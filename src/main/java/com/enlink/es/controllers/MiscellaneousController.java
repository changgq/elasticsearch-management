package com.enlink.es.controllers;

import com.enlink.es.base.BaseAction;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.services.MiscellaneousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 混合apis
 */
@RestController
public class MiscellaneousController extends BaseAction {

    @Autowired
    private MiscellaneousService miscellaneousService;

    @GetMapping("/api/info")
    public AjaxResults info() throws Exception {
        return Results.resultOf(ResultCode.OK, miscellaneousService.info());
    }

    @GetMapping("/api/ping")
    public AjaxResults ping() throws Exception {
        return Results.resultOf(ResultCode.OK, miscellaneousService.ping());
    }
}