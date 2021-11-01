package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;


@Data
@Builder
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private double totalAmount;
    private double tax;
    private Integer showTimesDetailId;
    private Integer userId;
    private String createDate;
    private String note;
    private Integer typeUser;
    private Integer creation;
    private String status;
    private Integer concessionId;
    private Integer seatId;
}
