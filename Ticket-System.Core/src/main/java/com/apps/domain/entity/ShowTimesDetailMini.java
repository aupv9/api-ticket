package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowTimesDetailMini implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String dayShowTimes;
    private String theaterName;
    private Integer theaterId;
    private String locationName;
}
