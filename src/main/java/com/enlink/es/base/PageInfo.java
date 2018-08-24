package com.enlink.es.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;

/**
 * 分页数据
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
public class PageInfo {
    private SearchHit[] data;
    private String scrollId;
    private int pageIndex;
    private int pageSize;
    private long total;
}
