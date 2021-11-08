package com.apps.domain.entity;


import com.apps.contants.PaymentStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Double amount = 0.d;
    private String transactionId = "";
    private String status = PaymentStatus.Pending.getValue();
    private Integer paymentMethodId = 1;
    private String createdDate,updatedDate;
    private String note;
    private String useFor = "Ticket";
    private Integer partId = 0, creation = 0,updatedBy = 0,userId = 0;

}
