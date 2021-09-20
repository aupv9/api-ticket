package com.apps.domain.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class SeatDataRoom implements Serializable {

    private Integer roomId;
    private Integer tierId;
    private Integer seatId;
    private Integer seatRoomId;
    private String tierName;
    private String seatName;
    private String status;
    private Float price;
    private Integer horizontalSize;
    private Integer verticalSize;

}
