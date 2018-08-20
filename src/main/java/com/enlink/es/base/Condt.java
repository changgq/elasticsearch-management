package com.enlink.es.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Condt {
    private String cycleType;
    private String cycle;
    private Map<String, Object> terms;
    private Map<String, Object> fuzziness;
    private Map<String, Object> d;
    private int pageIndex;
    private int pageSize;
}

