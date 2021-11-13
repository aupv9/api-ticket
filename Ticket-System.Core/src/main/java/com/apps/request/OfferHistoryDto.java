package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferHistoryDto {
    private String name,type,message,rule,startDate,
            endDate,updatedDate,method,creationDate,status;
    private int creationBy,updatedBy,maxTotalUsage,maxUsagePerUser,id;
    private double discountAmount,maxDiscount,percentage;
    private boolean anonProfile,allowMultiple;
}
