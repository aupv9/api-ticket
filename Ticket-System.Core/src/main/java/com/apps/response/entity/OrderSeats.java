package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSeats implements Serializable {

     private static final long serialVersionUID = -2541672333875751258L;
     private Integer ordersId,seatId,numbers;
     private String seatType,tier;
}
