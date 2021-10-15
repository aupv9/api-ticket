package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private Timestamp createDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private String type;
    private String image;
}
