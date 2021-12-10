package com.apps.mybatis.mysql;

import com.apps.domain.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieRepository {
    List<Movie> findByLocationAndDate(@Param("location") int location, @Param("date") String date);


    List<Movie> findAll(@Param("offset") int offset,@Param("limit") int limit
            ,@Param("search") String search,@Param("sort") String sort,
                        @Param("order") String order);
    @Select("\n" +
            "select * from movie where id in\n" +
            "(select movie_id from showtimes_detail " +
            "where date(time_start) between #{startWeek} and #{endWeek})\n")
    List<Movie> findAllCurrentWeek(@Param("startWeek")String startWeek,
                                   @Param("endWeek")String endWeek);

    @Select("select * from movie where id in\n" +
            "(select movie_id from showtimes_detail where date(time_start) > #{dateGte})")
    List<Movie> findAllComingSoon(@Param("dateGte")String dateGte);


    int findAllCount(@Param("search") String search);

    Movie findById(@Param("id") Integer id);

    int update(@Param("movie") Movie movie);

    void delete(@Param("id") Integer id);
}
