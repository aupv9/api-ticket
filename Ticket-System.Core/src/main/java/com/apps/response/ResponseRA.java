package com.apps.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRA {
    private Object content;
    private Integer totalElements;
}
