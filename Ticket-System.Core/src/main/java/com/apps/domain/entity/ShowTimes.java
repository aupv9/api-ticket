package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class ShowTimes implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date dayShowTimes;
    private Timestamp creationDate;
    private Timestamp endDate;
    private Timestamp startDate;

}
