package com.apps.service;

import com.apps.domain.entity.Theater;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface TheaterService {
    List<Theater> findAll(Integer page, Integer size);
    int insert(Theater theater);
    Theater findById(Integer id);
    int update(Theater theater);
    void deleteById(Integer id);
    Theater findByLocation(Integer id);

}