package com.apps.mapper;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberDto implements Serializable {
    private static final long serialVersionUID = 863425978091282110L;

    private Integer id, userId;
    private String number,creationDate,startDate,endDate,level,cmnd,birthday;
    private Double point = 0.d;
    private Boolean profile = false;
}
