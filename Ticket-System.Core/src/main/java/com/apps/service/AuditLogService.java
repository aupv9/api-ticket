package com.apps.service;

import com.apps.domain.entity.AuditLog;
import com.apps.mapper.AuditLogDto;

import java.util.List;

public interface AuditLogService{
    int insert(AuditLog auditLog);
    List<AuditLogDto> findAll(Integer limit, Integer offset,
                              String sort,String order,String date);
    int  findCountAll(String date);
}
