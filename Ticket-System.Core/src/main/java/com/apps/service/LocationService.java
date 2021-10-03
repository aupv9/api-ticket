package com.apps.service;

import com.apps.domain.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    List<Location> findAll(Integer page, Integer size, String sort, String order, String search);
    Location findById(Integer id);
    int update(Location location);
    Location delete(int id);
    int insert(Location location);
}
