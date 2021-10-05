package com.apps.mybatis.mysql;

import com.apps.domain.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface SeatRepository {
    List<Seat> findAll(@Param("limit") Integer page, @Param("offset") Integer size,
                       @Param("sort") String sort, @Param("order") String order,
                       @Param("search") String search, @Param("room") Integer room);


    @Select("SELECT * FROM SEAT WHERE ID = #{id}")
    Seat findById(Integer id);
    List<Seat> findSeatInRoomByShowTimesDetail(@Param("showTimesDetailId") Integer showTimesDetailId, @Param("room") Integer roomId);
    int findCountAll(@Param("search") String search, @Param("room") Integer room);
}
