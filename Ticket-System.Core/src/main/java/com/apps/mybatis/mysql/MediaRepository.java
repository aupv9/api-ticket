package com.apps.mybatis.mysql;

import com.apps.domain.entity.Media;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MediaRepository {

    @Select("select * from media where id = #{id}")
    Media findById(@Param("id")Integer id);

    @Select("select * from media where path = #{path}")
    Media findByPath(@Param("path")String path);

}
