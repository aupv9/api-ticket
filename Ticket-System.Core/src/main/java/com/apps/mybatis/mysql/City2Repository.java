package com.apps.mybatis.mysql;

import com.apps.domain.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface City2Repository {
    @Select("select id, name, state, country from city where state = #{state}")
    City findByState( String state);
}
