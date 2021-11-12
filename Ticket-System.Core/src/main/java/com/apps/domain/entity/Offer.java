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
public class Offer implements Serializable {

    private static final long serialVersionUID = -7343788203462480175L;

    private String name,type,message,rule,startDate,
            endDate,updatedDate,method,creationDate;
    private int creationBy,updatedBy,maxTotalUsage,maxUsagePerUser,id;
    private double discountAmount,maxDiscount,percentage;
    private boolean anonProfile,allowMultiple;

}
