package com.apps.mybatis.mysql;

import com.apps.domain.entity.Cast;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CastRepository {

    @Select("select * from cast where name = #{name}")
    Cast findByName(@Param("name")String name);

    @Select("select * from cast where id = #{id}")
    Cast findById(@Param("id")Integer id);

}
