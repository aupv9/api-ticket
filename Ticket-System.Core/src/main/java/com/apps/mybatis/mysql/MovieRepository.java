package com.apps.mybatis.mysql;

import com.apps.domain.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieRepository {
    List<Movie> findByLocationAndDate(@Param("location") int location, @Param("date") String date);
}
