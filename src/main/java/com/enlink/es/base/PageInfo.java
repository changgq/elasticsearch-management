package com.enlink.es.base;

import com.enlink.es.models.GeneralModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;

import java.util.List;

/**
 * 分页数据
 *
 * @author changgq
 */
@Data
@NoArgsConstructor
public class PageInfo<T extends GeneralModel> {
    private List<T> data;
    private int pageIndex;
    private int pageSize;
    private long total;
    private String scrollId;
}
