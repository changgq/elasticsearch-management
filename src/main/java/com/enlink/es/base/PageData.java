package com.enlink.es.base;

import com.enlink.es.models.GeneralModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.search.SearchHit;

import java.util.List;

@Data
@AllArgsConstructor
public class PageData {
    private int pageIndex;
    private int pageSize;
    private long total;
    private SearchHit[] data;
}
