package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class ShowTimesDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String timeStart;
    private String timeEnd;
    private int id;
    private Integer movieId;
    private Integer locationId;
    private Integer roomId;
    private Integer showTimesId;
    private Integer theaterId;
}