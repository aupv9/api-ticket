package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;


@Data
@Builder
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private float tax;
    private Integer showTimesDetailId;
    private Integer userId;
    private String createdDate;
    private String note;
    private Integer nonProfile = 0;
    private Integer creation;
    private String status;
    private String expirePayment;
}
