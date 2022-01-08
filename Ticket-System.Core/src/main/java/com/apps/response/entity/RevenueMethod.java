package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueMethod implements Serializable {
    private static final long serialVersionUID = -5458478015715021541L;
    private String id,label;
    private double value;
}
