package com.enlink.es.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 应用库
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppLibrary extends GeneralModel {
    // App名称
    private String app_name;
    // 描述
    private String description;
    // App urls
    private List<String> app_urls;
}
