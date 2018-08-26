package com.enlink.es.controllers;

import com.enlink.es.base.BaseAction;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.models.AppLibrary;
import com.enlink.es.services.AppLibraryRepository;
import com.enlink.es.utils.DateUtils;
import com.enlink.es.utils.ExcelUtils;
import com.enlink.es.utils.GsonUtils;
import com.enlink.es.utils.SecurityUtils;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    /**
     * 增加应用库
     * @param appLibrary
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public AjaxResults add(@RequestBody AppLibrary appLibrary) throws Exception {
        if (Strings.isBlank(appLibrary.getId())) {
            appLibrary.setId(SecurityUtils.md5(appLibrary.getApp_name()));
            appLibrary.setCreate_at(DateUtils.datetime2string(new Date()));
        }
        appLibraryRepository.saveOrUpdate(appLibrary);
        return Results.resultOf(ResultCode.OK, null);
    }

    /**
     * 删除应用库
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/delete/{id}")
    public AjaxResults delete(@PathVariable String id) throws Exception {
        appLibraryRepository.delete(id);
        return Results.resultOf(ResultCode.OK, null);
    }

    /**
     * 按id查询应用数据
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/get/{id}")
    public AjaxResults get(@PathVariable String id) throws Exception {
        return Results.resultOf(ResultCode.OK, appLibraryRepository.findById(id));
    }

    /**
     * 分页查询应用数据
     * @param searchCond
     * @return
     * @throws Exception
     */
    @PostMapping("/list")
    public AjaxResults paging(@RequestBody SearchCond searchCond) throws Exception {
        PageInfo pageInfo = appLibraryRepository.findByPaging(searchCond);
        System.out.println(GsonUtils.convert(pageInfo));
        return Results.resultOf(ResultCode.OK, pageInfo);
    }

    /**
     * 批量导入功能（预留）
     * @param request
     * @param response
     * @return
     */
    public AjaxResults batchImport(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws Exception {
        List<Map<String, String>> da = ExcelUtils.getExcelData(file.getInputStream(), false);
        if (null != da && da.size() > 0) {
            for (Map<String, String> d : da) {
                AppLibrary appLibrary = new AppLibrary();
                appLibrary.setApp_name(d.get("A"));
                appLibrary.setDescription(d.get("B"));
                String appUrls = d.get("C");
                if (Strings.isNotBlank(appUrls)) {
                    String[] _appUrls = appUrls.split(",");
                    appLibrary.setApp_urls(Arrays.asList(_appUrls));
                }
                appLibrary.setId(SecurityUtils.md5(appLibrary.getApp_name()));
                appLibrary.setCreate_at(DateUtils.datetime2string(new Date()));
                appLibraryRepository.saveOrUpdate(appLibrary);
            }
        }
        return Results.resultOf(ResultCode.OK, null);
    }
    /**
     * 应用库导出（预留）
     * @param request
     * @param response
     * @return
     */
    public AjaxResults exports(HttpServletRequest request, HttpServletResponse response) {

        return Results.resultOf(ResultCode.OK, null);
    }
}