package com.apps.mybatis.mysql;

import com.apps.domain.entity.Concession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConcessionRepository {
    List<Concession> findAll(@Param("limit") int limit, @Param("offset") int offset,
                             @Param("sort")String sort, @Param("order") String order,
                             @Param("name")String name, @Param("category") Integer categoryId);
    int findCountAll(@Param("name")String name, @Param("category") Integer categoryId);
    int update(@Param("entity") Concession concession);
    void delete(@Param("id") Integer id);
    @Select("SELECT * FROM CONCESSION WHERE ID = #{id}")
    Concession findById(@Param("id") Integer id);
}
