package com.apps.service.impl;

import com.apps.contants.Utilities;
import com.apps.domain.entity.AuditLog;
import com.apps.mapper.AuditLogDto;
import com.apps.mapper.Author;
import com.apps.mybatis.mysql.AuditLogRepository;
import com.apps.service.AuditLogService;
import com.apps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    private final UserService userService;

    @Override
    public int insert(AuditLog auditLog) {
        return this.auditLogRepository.insert(auditLog);
    }

    @Override
    @Cacheable(cacheNames = "AuditLogService",key = "'findAllAuditLog_'+#limit +'-'+#offset +'-'+#sort +'-'+#order +'-'+#date",unless = "#result == null ")
    public List<AuditLogDto> findAll(Integer limit, Integer offset, String sort,String order,String date) {
        var listAuditLogs = this.auditLogRepository.findAll(limit,offset,sort,order,!date.isEmpty() ? Utilities.convertIsoToDate(date): null);
        return this.addAuthorToAudit(listAuditLogs);
    }

    public List<AuditLogDto> addAuthorToAudit(List<AuditLogDto> auditLogDtos){
        auditLogDtos.forEach(auditLogDto -> {
            var user = this.userService.findUserByEmail(auditLogDto.getAccountName());
            if(user != null){
                var author = Author.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .avatar(user.getPhoto())
                        .build();
                auditLogDto.setAuthor(author);
            }
        });
        return auditLogDtos;
    }


    @Override
    @Cacheable(cacheNames = "AuditLogService",key = "'findCountAll_'+#date",unless = "#result == null ")
    public int findCountAll(String date) {
        return this.auditLogRepository.findCountAll(Utilities.convertIsoToDate(date));
    }

}
