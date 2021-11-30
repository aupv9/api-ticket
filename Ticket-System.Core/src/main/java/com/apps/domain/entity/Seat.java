package com.apps.domain.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String seatType;
    private Integer roomId;
    private Integer numbers;
    private String tier;
    private Boolean isSelected = false;
    private Integer status;
}
