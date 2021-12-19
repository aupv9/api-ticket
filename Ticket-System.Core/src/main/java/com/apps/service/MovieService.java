package com.apps.service;

import com.apps.domain.entity.Movie;
import com.apps.request.MovieDto;
import com.apps.response.MovieResponse;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface MovieService {
    List<Movie> findByLocationAndDate( int location, String date);
    List<MovieResponse> findAll(int page, int size, String search, String sort, String order);
    int findAllCount(String search);
    Movie findById(int id);
    Movie findByName(String name);
    Movie findByCode(String code);

    int update(Movie movie);
    int insert(Movie movie) throws SQLException;
    void delete(Integer id);
    List<Movie> findAllCurrentWeek();
    List<Movie> findAllComingSoon();
    int insertMulti(MovieDto movieDto) throws SQLException;

}
