package com.apps.mybatis.mysql;

import com.apps.domain.entity.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LocationRepository {
    List<Location> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset);
}
