package com.apps.service;

import com.apps.domain.entity.Category;
import com.apps.domain.entity.Concessions;

import java.sql.SQLException;
import java.util.List;

public interface ConcessionsService {
    List<Category> findAll(int limit, int offset,
                           String sort, String order,
                           String name, double price, int categoryId);
    int findCountAll(String name,int categoryId);
    Concessions findById(Integer id);
    int update(Concessions concessions);
    void delete(Integer id);
    int insert(Concessions concessions) throws SQLException;
}
