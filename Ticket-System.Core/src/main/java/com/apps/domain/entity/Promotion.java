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
public class Promotion implements Serializable {

    private static final long serialVersionUID = -7343788203462480175L;

    private Integer id,priority,uses,timeUtilExpire;
    private String creationDate,startDate,endDate,displayName
                    ,description,promotionType,beginUsable,endUsable;
    private boolean enable,anonProfile,allowMultiple,global;

}
