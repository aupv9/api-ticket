package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Theater implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String code;
    private String name;
    private String latitude;
    private String longitude;
    private Integer locationId;
    private String locationName;
    private String thumbnail;
    private String image;
}
