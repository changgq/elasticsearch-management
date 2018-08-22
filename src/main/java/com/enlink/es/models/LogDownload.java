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
    private String id;
    private String file_name;
    private long file_size;
    private float percent;
    private String log_type;
    private long download_count;
}
