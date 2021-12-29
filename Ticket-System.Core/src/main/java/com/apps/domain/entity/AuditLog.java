package com.apps.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {
    private int id;
    private String name,resourceName,accountName,
            objectName,action,actionDate,actionStatus;

}
