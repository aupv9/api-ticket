package com.apps.domain.repository;

import com.apps.domain.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City,Integer>, JpaSpecificationExecutor<City> {
    City findCityByState(String state);
}
