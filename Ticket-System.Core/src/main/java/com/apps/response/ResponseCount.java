package com.apps.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCount {
    private Integer id;
    private Integer count;
}
