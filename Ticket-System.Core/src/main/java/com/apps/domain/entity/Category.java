package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private String type;
    private String image;
    private String thumbnail;


}
