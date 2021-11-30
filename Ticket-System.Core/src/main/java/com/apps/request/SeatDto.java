package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDto implements Serializable {
    private static final long serialVersionUID = -219252588585659787L;
    private int id;
    private String seatType;
    private Integer roomId;
    private Integer numbers;
    private String tier;
}

