package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class SeatRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String status;
    private Integer user;
    private Integer seatId;
    private Integer roomId;
    private Integer tierId;
    private Integer showTimesDetailId;
}
