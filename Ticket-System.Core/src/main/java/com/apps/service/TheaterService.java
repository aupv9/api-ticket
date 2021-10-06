package com.apps.service;

import com.apps.domain.entity.Theater;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface TheaterService {
    List<Theater> findAll( Integer limit, Integer offset,
                           String sort, String order,
                           String search,
                           Integer location);

    int insert(Theater theater);
    Theater findById(Integer id);
    int update(Theater theater);
    void deleteById(Integer id);
    Theater findByLocation(Integer id);
    int findCountAll( String search, Integer location);
}
