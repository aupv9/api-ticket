package com.apps.domain.entity;

import lombok.Data;

@Data
public class Employee {
    private Integer userId, roleId, theater_id, createdBy, id;
    private String createdAt, status,updatedAt,startsAt, endsAt, notes;
}
