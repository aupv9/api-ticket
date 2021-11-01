package com.apps.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RAResponseUpdate {
    private Integer id;
    private Object data;
    private Object previousData;

}
