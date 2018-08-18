package com.enlink.es.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserLoginCount {
    private String id;
    private Date timestamp;
    private String username;
    private String userGroup;
    private String fullName;
    private String loginCount;
    private Date createAt;
}
