package com.apps.request;

import lombok.Data;

@Data
public class QueryParam {
    private Integer limit;
    private Integer offset;
    private String order;
    private String search;
    private String sort;
}
