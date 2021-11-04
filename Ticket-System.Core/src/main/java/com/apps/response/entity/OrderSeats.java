package com.apps.response.entity;

import lombok.Data;

@Data
public class OrderSeats {

//    orders_id,seat_id, seat_type, tier, numbers,
//    price
     private Integer ordersId,seatId,numbers;
     private Double price;
     private String tier, seatType;
}
