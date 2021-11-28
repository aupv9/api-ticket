package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class ShowTimesDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String timeStart,roomName;
    private String timeEnd;
    private Integer id;
    private Integer movieId;
    private Integer roomId;
    private String date;
    private Integer theaterId;

}
