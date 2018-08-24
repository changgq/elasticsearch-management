package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 日志下载记录
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LogDownload extends GeneralModel {
    // 文件名
    private String file_name;
    // 文件存放路径
    private String file_path;
    // 文件大小
    private long file_size;
    // 生成状态
    private float percent;
    // 日志类型
    private String log_type;
}
