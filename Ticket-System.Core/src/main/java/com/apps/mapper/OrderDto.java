package com.apps.mapper;

import lombok.Data;

@Data
public class OrderDto {
    private int id;
    private double totalAmount;
    private double tax;
    private Integer showTimesDetailId;
    private Integer userId;
    private String createDate;
    private String note;
    private Boolean typeUser = false;
    private Integer creation;
    private String status;
    private Integer[] concessionId;
    private Integer[] seats;
    private Integer room;

}
