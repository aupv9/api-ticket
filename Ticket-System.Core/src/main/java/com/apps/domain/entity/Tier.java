package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Tier implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String code;
    private String name;
    private Integer countSeat;
    private Integer roomBId;

}
