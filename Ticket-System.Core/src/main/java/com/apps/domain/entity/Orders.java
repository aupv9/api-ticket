package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;


@Data
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date ticketDate;
    private double totalAmount;
    private double tax;
    private Integer showTimesDetailId;
    private Integer ordersId;
    private Integer userId;
    private Date createDate;
    private String note;
}
