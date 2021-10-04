package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String code;
    private String name;
    private Integer theaterId;
    private String type;
}
