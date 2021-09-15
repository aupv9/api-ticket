package com.apps.mybatis.mysql;

import com.apps.domain.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeatRepository {
    List<Seat> findAll(@Param("limit") Integer page, @Param("offset") Integer size);
    @Select("SELECT * FROM SEAT WHERE ID = #{id}")
    Seat findById(Integer id);
}
