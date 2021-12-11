package com.apps.response.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class ConcessionMyOrder implements Serializable {
    private static final long serialVersionUID = -5762039336463501358L;
    private String name;
    private Double price;
    private Integer categoryId;
    private Integer quantity;
    private Integer ordersId;
    private Integer concessionId;
}
