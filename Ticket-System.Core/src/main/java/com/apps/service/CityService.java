package com.apps.service;

import com.apps.domain.entity.City;

public interface CityService {
    City findByState(String state);
}
