package com.apps.service.impl;

import com.apps.domain.entity.Theater;
import com.apps.mybatis.mysql.TheaterRepository;
import com.apps.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;


    @Override
    @Cacheable(cacheNames = "TheaterService" , unless = "#result == null")
    public List<Theater> findAll(Integer page, Integer size) {
            return this.theaterRepository.findAll(size,page*size);
    }

    @Override
    public int insert(Theater theater) {
        return this.theaterRepository.insert(theater);
    }
}
