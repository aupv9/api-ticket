package com.apps.service.impl;

import com.apps.domain.entity.City;
import com.apps.mybatis.mysql.City2Repository;
import com.apps.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    City2Repository city2Repository;

    @Cacheable(value = "CityService", key = "'CityService.findByState_'+#state", unless = "#result == null")
    @Override
    public City findByState(String state) {
        simulateSlowService();
        return city2Repository.findByState(state);
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
