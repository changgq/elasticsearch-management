package com.enlink.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopRankingDto {
    private String topRankingKey;
    private long topRankingValue;
}
