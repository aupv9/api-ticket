package com.apps.mybatis.mysql;

import com.apps.domain.entity.Category;
import com.apps.domain.entity.Concessions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConcessionsRepository {
    @Select("Select * from category")
    List<Category> findAll(@Param("limit") int limit, @Param("offset") int offset,
                           @Param("sort")String sort, @Param("order") String order,
                           @Param("name")String name, @Param("price")Double price,
                           @Param("categoryId") int categoryId);

    int findCountAll(@Param("name")String name, @Param("categoryId") int categoryId);
    int update(@Param("entity") Concessions concessions);
    void delete(@Param("id") Integer id);
    @Select("SELECT * FROM CONCESSION WHERE ID = #{id}")
    Concessions findById(@Param("id") Integer id);
}
