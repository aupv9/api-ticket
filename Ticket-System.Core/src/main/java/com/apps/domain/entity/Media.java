package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media implements Serializable {
    private static final long serialVersionUID = -8592184543690948287L;
    private Integer id;
    private String creationDate,startDate,endDate,description,name,path,mediaType;
}
