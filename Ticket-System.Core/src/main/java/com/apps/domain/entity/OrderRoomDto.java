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
public class OrderRoomDto  implements Serializable {
    private static final long serialVersionUID = 4397172531321858408L;
    private double tax;
    private Integer showTimesDetailId,userId,updatedBy = 0,creation,id;
    private String movieName,note,status,createdDate,expirePayment,
            updatedAt,timeStart,roomName,theaterName,locationName,timeEnd;
    private boolean profile = false;
    private double total;

}
