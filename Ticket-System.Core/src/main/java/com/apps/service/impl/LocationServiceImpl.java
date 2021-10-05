package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Location;
import com.apps.domain.repository.LocationCustomRepository;
import com.apps.mybatis.mysql.LocationRepository;
import com.apps.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {


    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationCustomRepository locationCustomRepository;

    @Autowired
    ApplicationCacheManager cacheManager;

    @Override
    @Cacheable(value = "LocationService" ,key = "'LocationList_'+#page +'-'+#size +'-'+#sort +'-'+#order +'-'+#search", unless = "#result == null")
    public List<Location> findAll(Integer page, Integer size,String sort, String order, String search) {
        return this.locationRepository.findAll(size, page * size,sort,order, search);
    }

    @Override
    @Cacheable(value = "LocationService" ,key = "'findCountAll_'+#search", unless = "#result == null")
    public int findCountAll(String search) {
        return this.locationRepository.findCountAll(search);
    }

    @Override
    @Cacheable(value = "LocationService" ,key = "'findById_'+#id", unless = "#result == null")
    public Location findById(Integer id) {
        return this.locationRepository.findById(id);
    }

    @Override
    public int update(Location location) {
        Location location1  = this.findById(location.getId());
        location1.setName(location.getName());
        location1.setZipcode(location.getZipcode());
        int result = this.locationRepository.update(location1);
        cacheManager.clearCache("LocationService");
        return result;
    }

    @Override
    @CacheEvict(cacheNames = "LocationService",key = "'findById_'+#id")
    public Location delete(int id) {
        Location location  = this.findById(id);
        this.locationRepository.delete(id);
        cacheManager.clearCache("LocationService");
        return location;
    }

    @Override
    public int insert(Location location) throws SQLException {
        String sql = "INSERT INTO location (name,zipcode) VALUES (?,?)";
        int result = this.locationCustomRepository.insert(location,sql);
        cacheManager.clearCache("LocationService");
        return result;
    }


}
