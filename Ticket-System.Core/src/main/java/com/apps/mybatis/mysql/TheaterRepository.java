package com.apps.mybatis.mysql;


import com.apps.domain.entity.Theater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TheaterRepository {
    List<Theater> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset);
    int insert(@Param("entity") Theater theater);
    Theater findById(@Param("id") Integer id);
    int update(@Param("entity") Theater theater);
    void delete(Integer id);
    Theater findByLocation(@Param("id") Integer idLocation);
}
