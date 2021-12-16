package com.apps.mybatis.mysql;

import com.apps.domain.entity.Movie;
import org.apache.ibatis.annotations.Insert;
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

    @Insert("insert into movie_cast(movie_id,name_cast) values(#{movie},#{name})")
    int insertMovieCast(@Param("movie")Integer movie,@Param("name")String cast);

    @Insert("insert into movie_tags(movie_id,tag_name) values(#{movie},#{tag})")
    int insertMovieTag(@Param("movie")Integer movie,@Param("tag")String tag);

    @Insert("insert into movie_media(movie_id,media_id) values(#{movie},#{media})")
    int insertMovieMedia(@Param("movie")Integer movie,@Param("media")Integer media);

    @Select("select cast_id from movie_cast where movie_id = #{movie}")
    List<Integer> findAllCastInMovie(@Param("movie")Integer movie);
}
