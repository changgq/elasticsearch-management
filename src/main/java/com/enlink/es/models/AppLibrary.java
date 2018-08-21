package com.enlink.es.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 应用库
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
public class AppLibrary extends GeneralModel {
    private String id;
    private String app_name;
    private String description;
    private List<String> app_urls;
    private Date create_at;
}
