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
public class ShowTimesDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String timeStart,roomName,status;
    private String timeEnd;
    private Integer id;
    private Integer movieId;
    private Integer roomId;
    private String date;
    private Integer theaterId;
    private double price;
    private boolean reShow;

}
