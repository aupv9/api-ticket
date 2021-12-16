package com.apps.mybatis.mysql;

import com.apps.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TagRepository {

    @Select("select * from movie_type where name = #{name}")
    Tag findByName(@Param("name")String name);
}
