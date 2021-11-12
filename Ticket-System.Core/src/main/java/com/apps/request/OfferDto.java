package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfferDto {
    private String name,type,message,rule,startDate,
            endDate,updatedDate,method,creationDate,code;
    private int creationBy,updatedBy,maxTotalUsage,maxUsagePerUser,typePromo,id,countCode;
    private double discountAmount,maxDiscount,percentage;
    private boolean anonProfile,allowMultiple;
    private Integer[] movieIds;

}
