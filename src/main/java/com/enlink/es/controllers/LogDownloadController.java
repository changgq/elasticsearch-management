package com.enlink.es.controllers;

import com.enlink.es.base.BaseAction;
import com.enlink.es.base.PageInfo;
import com.enlink.es.base.SearchCond;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.models.LogDownload;
import com.enlink.es.services.LogDownloadRepository;
import com.enlink.es.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 日志下载
 *
 * @author changgq
 */
@RestController
@RequestMapping("/api/logDownload/")
public class LogDownloadController extends BaseAction {

    @Autowired
    private LogDownloadRepository logDownloadRepository;

    /**
     * 日志下载列表
     *
     * @param searchCond
     * @return
     * @throws Exception
     */
    @PostMapping("/list")
    public AjaxResults list(@RequestBody SearchCond searchCond) throws Exception {
        PageInfo pageInfo = logDownloadRepository.findByPaging(searchCond);
        return Results.resultOf(ResultCode.OK, pageInfo);
    }

    /**
     * 保存日志下载记录
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public AjaxResults add(@RequestBody LogDownload model) throws Exception {
        logDownloadRepository.saveOrUpdate(model);
        return Results.resultOf(ResultCode.OK, null);
    }

    /**
     * 下载
     *
     * @param id
     * @param response
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws Exception {
        LogDownload logDownload = GsonUtils.reConvert2Object(logDownloadRepository.findById(id), LogDownload.class);
        File df = new File(logDownload.getFile_path());
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLengthLong(df.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(logDownload.getFile_name().getBytes()));
        byte[] buff = new byte[2048];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(df));
            int len;
            while ((len = bis.read(buff)) > 0) {
                os.write(buff, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
