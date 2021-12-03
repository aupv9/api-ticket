package com.apps.domain.entity;

import lombok.Data;

@Data
public class Employee {
    private Integer userId, theaterId, createdBy, id,updatedBy;
    private String createdAt, status,updatedAt,startsAt, endsAt, notes;
}
