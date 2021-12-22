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
public class PercentCoverRoom implements Serializable {
    private static final long serialVersionUID = 3952058312386847189L;
    private String id,label;
    private double value;
}
