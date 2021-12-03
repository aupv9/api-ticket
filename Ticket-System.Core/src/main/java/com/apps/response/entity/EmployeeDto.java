package com.apps.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto implements Serializable {
    private static final long serialVersionUID = -3792716562773247480L;
    private Integer userId, theaterId, createdBy, id,updatedBy;
    private String createdAt, status,updatedAt,startsAt, endsAt, notes;
    private List<Integer> roleIds;
}
