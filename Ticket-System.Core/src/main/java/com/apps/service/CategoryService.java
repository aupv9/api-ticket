package com.apps.service;

import com.apps.domain.entity.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {
    List<Category> findAll( Integer limit, Integer offset,
                          String sort,String order,
                          String name, String type);
    int findCountAll(String name,String type);
    Category findById(Integer id);
    int update(Category category);
    void delete(Integer id);
    int insert(Category category) throws SQLException;
}
