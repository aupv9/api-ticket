package com.apps.mybatis.mysql;

import com.apps.domain.entity.AuditLog;
import com.apps.mapper.AuditLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditLogRepository {
    int insert(@Param("audit") AuditLog auditLog);
    List<AuditLogDto> findAll(@Param("limit") int limit, @Param("offset") int offset,
                              @Param("sort")String sort, @Param("order") String order,@Param("actionDate") String date);
    int findCountAll(@Param("actionDate") String date);
}
