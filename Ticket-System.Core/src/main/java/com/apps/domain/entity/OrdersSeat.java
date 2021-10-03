package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersSeat implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer ordersId;
    private Integer seatId;
}
