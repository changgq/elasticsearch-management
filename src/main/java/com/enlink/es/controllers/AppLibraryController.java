package com.enlink.es.controllers;

import com.enlink.es.base.Condt;
import com.enlink.es.base.PageData;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.models.AppLibrary;
import com.enlink.es.services.AppLibraryService;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.GsonUtils;
import com.enlink.es.utils.IDUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * app库
 *
 * @author changgq
 */

@RestController
@RequestMapping("/api/applib")
public class AppLibraryController {

    @Autowired
    private AppLibraryService appLibraryService;

    @PostMapping("/add")
    public AjaxResults add(@RequestBody AppLibrary appLibrary) throws Exception {
        if (null != appLibrary && Strings.isBlank(appLibrary.getId())) {
            appLibrary.setId(IDUtils.genIdx32());
            appLibrary.setCreate_at(DateUtils.datetime2string(new Date()));
        }
        appLibraryService.saveOrUpdate(appLibrary.getId(), GsonUtils.convert(appLibrary));
        return Results.resultOf(ResultCode.OK, null);
    }

    @GetMapping("/delete/{id}")
    public AjaxResults delete(@PathVariable String id) throws Exception {
        appLibraryService.delete(id);
        return Results.resultOf(ResultCode.OK, null);
    }

    @GetMapping("/get/{id}")
    public AjaxResults get(@PathVariable String id) throws Exception {
        return Results.resultOf(ResultCode.OK, appLibraryService.findById(id));
    }

    @PostMapping("/paging")
    public AjaxResults paging(Condt condt) throws Exception {
        PageData pageData = appLibraryService.findByPaging(condt, condt.getPageIndex(), condt.getPageSize());
        return Results.resultOf(ResultCode.OK, pageData);
    }
}