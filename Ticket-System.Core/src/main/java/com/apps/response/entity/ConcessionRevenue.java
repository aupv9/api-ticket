package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcessionRevenue implements Serializable {
    private static final long serialVersionUID = -8180545367591916997L;
    private String id,label;
    private double value;
}
