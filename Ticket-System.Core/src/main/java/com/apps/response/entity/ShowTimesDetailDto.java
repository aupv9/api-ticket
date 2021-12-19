package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimesDetailDto implements Serializable {
    private static final long serialVersionUID = -6300455129929864210L;
    private String timeStart,roomName;
    private String timeEnd,status,releasedDate;
    private Integer id,countSeatAvailable;
    private Integer movieId;
    private Integer roomId;
    private Integer theaterId;
    private double price;
    private boolean reShow;
}
