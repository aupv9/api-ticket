package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Float price;
    private Integer seatTypeId;
    private Integer tierId;
    private String name;
}
