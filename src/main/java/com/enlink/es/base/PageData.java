package com.enlink.es.base;

import com.enlink.es.models.GeneralModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageData<T extends GeneralModel> {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer total;
    private List<T> data;
}
