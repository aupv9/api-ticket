package com.apps.mybatis.mysql;

import com.apps.domain.entity.Concessions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConcessionRepository {
    @Select("Select * from concession")
    List<Concessions> findAll();
}
