package com.apps.response.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class MyOrderResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private double totalAmount;
    private float tax;
    private Integer showTimesDetailId;
    private Integer userId;
    private String createdDate;
    private String note;
    private Integer typeUser = 0;
    private Integer creation;
    private String status;
    private String expirePayment;
    private List<ConcessionMyOrder> concessions;
    private List<OrderSeats> seats;

}
