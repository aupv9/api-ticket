package com.apps.service.impl;

import com.apps.domain.entity.Location;
import com.apps.mybatis.mysql.LocationRepository;
import com.apps.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {


    @Autowired
    LocationRepository locationRepository;

    @Override
    @Cacheable(value = "LocationService" ,key = "'LocationList_'+#page +'-'+#size", unless = "#result == null")
    public List<Location> findAll(Integer page, Integer size) {
        return this.locationRepository.findAll(size,size * page);
    }

    @Override
    public Location findById(Integer id) {
        return this.locationRepository.findById(id);
    }

}
