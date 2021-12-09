package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Movie;
import com.apps.domain.repository.MovieCustomRepository;
import com.apps.mybatis.mysql.MovieRepository;
import com.apps.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ApplicationCacheManager cacheManager;

    @Autowired
    private MovieCustomRepository movieCustomRepository;

    @Override
    public List<Movie> findByLocationAndDate(int location, String date) {
        return movieRepository.findByLocationAndDate(location,date);
    }

    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findAll_'+#page+'-'+#size+'-'+#search+'-'+#sort+'-'+#order",
            unless = "#result == null ")
    public List<Movie> findAll(int page, int size,String search, String sort, String order) {
        return movieRepository.findAll(page * size, size,search,sort,order);
    }

    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findAllCountMovie_'+#search",
            unless = "#result == null ")
    public int findAllCount(String search) {
        return movieRepository.findAllCount(search);
    }

    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findByIdMovie_'+#id",
            unless = "#result == null ")
    public Movie findById(int id) {
        return movieRepository.findById(id);
    }

    @Override
    public int update(Movie movie) {
        Movie movie1 = findById(movie.getId());
        movie1.setName(movie.getName());
        movie1.setThumbnail(movie.getThumbnail());
        movie1.setImage(movie.getImage());
        int result = this.movieRepository.update(movie1);
        cacheManager.clearCache("MovieService");
        return result;
    }

    @Override
    public int insert(Movie movie) throws SQLException {
        String sql = "INSERT INTO MOVIE('name','thumbnail','image','active') VALUES(?,?,?,?)";
        return this.movieCustomRepository.insert(movie,sql);
    }

    @Override
    public void delete(Integer id) {
        Movie movie1 = findById(id);
        this.movieRepository.delete(id);
        cacheManager.clearCache("MovieService");
    }

    @Override
    public List<Movie> findAllCurrentWeek() {
        return this.movieRepository.findAllCurrentWeek(Utilities.startOfWeek("yyyy-MM-dd"),Utilities.currentWeekEndDate());
    }

    @Override
    public List<Movie> findAllComingSoon() {
        return this.movieRepository.findAllComingSoon(Utilities.currentWeekEndDate());
    }
}
