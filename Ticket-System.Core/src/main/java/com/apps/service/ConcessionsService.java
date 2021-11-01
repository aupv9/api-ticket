package com.apps.service;

import com.apps.domain.entity.Concession;

import java.sql.SQLException;
import java.util.List;

public interface ConcessionsService {
    List<Concession> findAll(int page, int size,
                             String sort, String order,
                             String name, Integer categoryId);
    int findCountAll(String name, Integer categoryId);
    Concession findById(Integer id);
    int update(Concession concession);
    void delete(Integer id);
    int insert(Concession concession) throws SQLException;
    List<Concession> findAll();
}
