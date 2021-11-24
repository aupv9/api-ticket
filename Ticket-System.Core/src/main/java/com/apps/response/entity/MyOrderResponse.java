package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private double totalAmount,discountAmount;
    private float tax;
    private Integer showTimesDetailId;
    private Integer userId;
    private String createdDate;
    private String note;
    private Boolean profile = false;
    private Integer creation,updatedBy;
    private String status;
    private String expirePayment,updatedDate;
    private List<ConcessionMyOrder> concessions;
    private List<OrderSeats> seats;


}
