package com.enlink.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NetworkTrafficDto {
    private String dateTime;
    private double inBytes;
    private double outBytes;
}
