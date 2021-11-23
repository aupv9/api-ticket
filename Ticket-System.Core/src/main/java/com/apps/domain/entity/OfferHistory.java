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
public class OfferHistory implements Serializable {
    private static final long serialVersionUID = -5347656550590785245L;
    private int id, userId,offerId,orderId;
    private String code, status, timeUsed;
    private double totalDiscount;
}
