package com.apps.mybatis.mysql;

import com.apps.domain.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieRepository {
    List<Movie> findByLocationAndDate(@Param("location") int location, @Param("date") String date);


    List<Movie> findAll(@Param("offset") int offset,@Param("limit") int limit
            ,@Param("search") String search,@Param("sort") String sort,
                        @Param("order") String order);

    int findAllCount(@Param("search") String search);

    Movie findById(@Param("id") Integer id);

    int update(@Param("movie") Movie movie);

    void delete(@Param("id") Integer id);
}
