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
    private String id;
    private String app_name;
    private String description;
    private List<String> app_urls;
}
