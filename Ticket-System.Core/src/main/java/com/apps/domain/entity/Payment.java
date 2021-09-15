package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Double amount;
    private String transactionId;
    private Integer status;
    private Integer paymentMethodId;

}
