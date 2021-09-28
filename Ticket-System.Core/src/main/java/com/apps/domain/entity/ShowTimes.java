package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class ShowTimes implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private Date dayShowTimes;
    private String creationDate;
    private String endDate;
    private String startDate;

}
