package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Concession implements Serializable {
    private static final long serialVersionUID = -3175222913193575507L;
    private Integer id;
    private String name;
    private Double price;
    private Integer categoryId;
}
