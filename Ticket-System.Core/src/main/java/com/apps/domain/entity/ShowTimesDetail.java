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
    private Integer roomId;
    private String date;
}
