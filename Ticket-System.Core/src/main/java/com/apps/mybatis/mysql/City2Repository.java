package com.apps.mybatis.mysql;

import com.apps.domain.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface City2Repository {
    City findByState(@Param("state") String state);
}
