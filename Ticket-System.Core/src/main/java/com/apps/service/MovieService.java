package com.apps.service;

import com.apps.domain.entity.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MovieService {
    List<Movie> findByLocationAndDate( int location, String date);

}
