package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;


@Data
@Builder
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private float tax;
    private Integer showTimesDetailId,userId,updatedBy = 0,creation,id;
    private String createdDate, updatedAt,note,status,expirePayment;
    private boolean profile = false;

}
