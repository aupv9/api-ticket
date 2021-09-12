package com.apps.domain.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class Location implements Serializable {
    private int id;
    private String name;
    private String zipcode;
    private String latitude;
    private String longitude;
}
