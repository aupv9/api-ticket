package com.apps.domain.entity;


import com.apps.contants.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Double amount = 0.d;
    private String status = PaymentStatus.Pending.getValue();
    private Integer paymentMethodId = 1;
    private String createdDate,updatedDate,note,transactionId;
    private String useFor = "Ticket",code = "";
    private Integer partId = 0, creation = 0,updatedBy = 0,userId = 0;

}
