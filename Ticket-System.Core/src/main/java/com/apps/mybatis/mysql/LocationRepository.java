package com.apps.mybatis.mysql;

import com.apps.domain.entity.Location;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LocationRepository {

    List<Location> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                           @Param("sort") String sort, @Param("order") String order,
                           @Param("search") String search);

    @Select("SELECT * FROM LOCATION WHERE ID = #{id}")
    Location findById(@Param("id") Integer id);

    int update(@Param("location") Location location);

    @Delete("Delete from location where id =#{id}")
    void delete(@Param("id") int id);

    int insert(@Param("entity") Location location);
}
