package com.enlink.es.controllers;

import com.enlink.es.base.BaseAction;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.models.AppLibrary;
import com.enlink.es.services.AppLibraryRepository;
import com.enlink.es.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * app库
 *
 * @author changgq
 */

@RestController
@RequestMapping("/api/appLibrary")
public class AppLibraryController extends BaseAction {

    @Autowired
    private AppLibraryRepository appLibraryRepository;

    @PostMapping("/add")
    public AjaxResults add(@RequestBody AppLibrary appLibrary) throws Exception {
        appLibraryRepository.saveOrUpdate(appLibrary);
        return Results.resultOf(ResultCode.OK, null);
    }

    @GetMapping("/delete/{id}")
    public AjaxResults delete(@PathVariable String id) throws Exception {
        appLibraryRepository.delete(id);
        return Results.resultOf(ResultCode.OK, null);
    }

    @GetMapping("/get/{id}")
    public AjaxResults get(@PathVariable String id) throws Exception {
        return Results.resultOf(ResultCode.OK, appLibraryRepository.findById(id));
    }

    @PostMapping("/list")
    public AjaxResults paging(SearchCond searchCond) throws Exception {
        PageInfo pageInfo = appLibraryRepository.findByPaging(searchCond);
        System.out.println(GsonUtils.convert(pageInfo));
        return Results.resultOf(ResultCode.OK, pageInfo);
    }
}