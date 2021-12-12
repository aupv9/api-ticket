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
public class Member implements Serializable {
    private static final long serialVersionUID = 353978417173006709L;

    private Integer id, userId;
    private String number,pin,creationDate,startDate,endDate,level,cmnd,birthday;
    private Double point = 0.d;
    private Boolean profile = false;
}
