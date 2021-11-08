package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private float tax;
    private Integer showTimesDetailId,userId,updatedBy = 0,creation,id;
    private String note,status;
    private boolean profile = false;

}
