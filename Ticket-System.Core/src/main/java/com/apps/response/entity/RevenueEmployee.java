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
public class RevenueEmployee implements Serializable {
    private static final long serialVersionUID = 1508569595806149475L;
    private Integer userId, theaterId, createdBy, id,updatedBy,countOrder;
    private String createdAt, status,updatedAt,startsAt, endsAt, notes,fullName,avatar;
    private List<Integer> roleIds;
    private boolean online;
    private double revenue;
}
