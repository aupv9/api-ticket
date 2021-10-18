package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Privilege implements Serializable {
    private static final long serialVersionUID = -5560995444239070765L;
    private Integer id;
    private String name;
}
