package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Double amount;
    private String transactionId;
    private String status;
    private Integer paymentMethodId;
    private String createtionDate;
    private String startDate;
    private String endDate;
    private String note;
    private String useFor;
    private Integer partId;

}
