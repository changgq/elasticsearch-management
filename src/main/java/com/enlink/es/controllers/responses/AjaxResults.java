package com.enlink.es.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AjaxResults {
    private String code;
    private String message;
    private Object data;
}
