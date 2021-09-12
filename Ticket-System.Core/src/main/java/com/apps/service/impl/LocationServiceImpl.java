package com.apps.service.impl;

import com.apps.domain.entity.Location;
import com.apps.jpa.repository.LocationRepositoryJPA;
import com.apps.mybatis.mysql.LocationRepository;
import com.apps.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepositoryJPA locationRepositoryJPA;
    @Autowired
    LocationRepository locationRepository;

    @Override
    @Cacheable(cacheNames = "LocationService" , unless = "#result == null")
    public List<Location> findAll(Integer page, Integer size) {
        return this.locationRepository.findAll(size,size * page);
    }

}
