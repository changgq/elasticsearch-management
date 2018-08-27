package com.enlink.es.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 查询条件
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
public class SearchCond {
    private int pageIndex;
    private int pageSize;
    private Map<String, String> terms;
    private Map<String, String> fuzziness;
    private Map<String, String> shoulds;
}

