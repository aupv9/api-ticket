package com.apps.domain.entity;

import lombok.Data;

@Data
public class Employee {
    private Integer userId, roleId, theaterId, createdBy, id,updatedBy;
    private String createdAt, status,updatedAt,startsAt, endsAt, notes;
}
