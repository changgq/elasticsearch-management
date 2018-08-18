package com.enlink.es.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Condt {
    private String cycleType;
    private String cycle;
    private String pageIndex;
    private String pageSize;
}

