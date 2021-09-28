package com.apps.service;

import com.apps.domain.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    List<Location> findAll(Integer page, Integer size);
    Location findById(Integer id);
}
