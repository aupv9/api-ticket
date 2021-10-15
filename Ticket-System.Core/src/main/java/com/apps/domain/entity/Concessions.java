package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Concessions implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Double price;
    private Integer categoryId;
    private String image;
}
