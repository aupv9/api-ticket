package com.apps.request;

import lombok.Data;

@Data
public class MyOrderUpdateDto {
    private int id;
    private Integer userId;
    private String status;
    private Boolean typeUser = false;
    private String note;
    private double total;
}
