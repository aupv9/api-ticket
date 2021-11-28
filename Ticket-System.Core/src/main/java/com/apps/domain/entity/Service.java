package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Service implements Serializable {
    private static final long serialVersionUID = 8607729621635261992L;
    private int id;
    private String name,description,thumbnail;
}
