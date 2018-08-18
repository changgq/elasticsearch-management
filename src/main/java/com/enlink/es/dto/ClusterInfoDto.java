package com.enlink.es.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClusterInfoDto {
    private String name;
    private String clusterName;
    private String clusterUuid;
    private String version;
    private String createTime;
}
