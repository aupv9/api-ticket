package com.apps.mybatis.mysql;

import com.apps.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryRepository {
    List<Category> findAll(@Param("limit") int limit, @Param("offset") int offset,
                           @Param("sort")String sort, @Param("order") String order,
                           @Param("name")String name, @Param("type")String type);
    int findCountAll(@Param("name")String name, @Param("type")String type);
    int update(@Param("entity") Category category);
    void delete(@Param("id") Integer id);
    Category findById(@Param("id") Integer id);
}

