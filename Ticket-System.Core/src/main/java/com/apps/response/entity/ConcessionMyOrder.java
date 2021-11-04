package com.apps.response.entity;


import lombok.Data;

@Data
public class ConcessionMyOrder {
    private String name;
    private Double price;
    private Integer categoryId;
    private Integer quantity;
    private Integer ordersId;
    private Integer concessionId;
}
