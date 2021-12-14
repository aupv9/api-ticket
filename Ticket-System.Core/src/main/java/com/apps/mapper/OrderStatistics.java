package com.apps.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatistics implements Serializable {
    private static final long serialVersionUID = 7847853057205809339L;
    private int id;
    private String movieName,status,createdDate,expirePayment,
            roomName,theaterName,locationName;
    private boolean profile = false;
    private double total;
}
