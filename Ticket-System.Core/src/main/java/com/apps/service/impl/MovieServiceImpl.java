package com.apps.service.impl;

import com.apps.domain.entity.Movie;
import com.apps.mybatis.mysql.MovieRepository;
import com.apps.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> findByLocationAndDate(int location, String date) {
        return movieRepository.findByLocationAndDate(location,date);
    }
}
