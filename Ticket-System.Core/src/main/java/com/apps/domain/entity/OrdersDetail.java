package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Double amount;
    private Integer foodId;
    private Integer ordersId;
}
