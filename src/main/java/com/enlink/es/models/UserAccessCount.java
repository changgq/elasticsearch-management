package com.enlink.es.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserAccessCount {
    private String id;
    private String type;
    private String cycle;
    private String username;
    private String userGroup;
    private String fullName;
    private String accessCount;
    private Date createAt;
}
