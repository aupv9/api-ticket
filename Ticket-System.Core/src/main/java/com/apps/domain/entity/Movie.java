package com.apps.domain.entity;
import lombok.Data;
import java.io.Serializable;

@Data
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String thumbnail;
    private String image;
}
