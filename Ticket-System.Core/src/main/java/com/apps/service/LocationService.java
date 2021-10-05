package com.apps.service;

import com.apps.domain.entity.Location;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;

public interface LocationService {
    List<Location> findAll(Integer page, Integer size, String sort, String order, String search);
    int findCountAll(@Param("search") String search);
    Location findById(Integer id);
    int update(Location location);
    Location delete(int id);
    int insert(Location location) throws SQLException;
}
