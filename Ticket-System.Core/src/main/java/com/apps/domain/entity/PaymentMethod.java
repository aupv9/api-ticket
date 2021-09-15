package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String code;
    private String name;
}
