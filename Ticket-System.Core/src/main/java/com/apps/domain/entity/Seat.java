package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Float price;
    private String seatType;
    private Integer roomId;
    private String name;
    private Integer numbers;
    private String tier;
}
