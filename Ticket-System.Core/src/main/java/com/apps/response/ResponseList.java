package com.apps.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseList {
    private Object data;
    private Integer total;
}
