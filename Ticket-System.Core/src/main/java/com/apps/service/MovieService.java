package com.apps.service;

import com.apps.domain.entity.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieService {
    List<Movie> findByLocationAndDate( int location, String date);
    List<Movie> findAll(int page, int size,String search, String sort, String order);
    int findAllCount(String search);
    Movie findById(int id);
    int update(Movie movie);
    int insert(Movie movie) throws SQLException;
    void delete(Integer id);
}
