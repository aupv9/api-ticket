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
    private Integer showTimesDetailId,userId,updatedBy = 0,creation,id;
    private String movieName,status,createdDate,roomName,theaterName,locationName,timeStart;
    private boolean profile = false,isOnline = false;
    private double total,tax,totalSeats,totalConcessions;
}
