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
public class PercentPaymentMethod implements Serializable {

    private static final long serialVersionUID = 1298438533247780658L;
    private String id,label;
    private double value;
}
