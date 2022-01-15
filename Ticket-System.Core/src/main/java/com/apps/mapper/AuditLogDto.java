package com.apps.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogDto implements Serializable {
    private static final long serialVersionUID = 9209239807711251498L;
    private int id;
    private String name,resourceName,accountName,
            objectName,action,actionDate,actionStatus;
    private Author author;
}
