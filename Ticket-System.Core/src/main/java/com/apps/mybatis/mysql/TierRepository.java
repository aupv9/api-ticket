package com.apps.mybatis.mysql;


import com.apps.domain.entity.Tier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TierRepository {
    List<Tier> findAll(@Param("limit") int limit,
                       @Param("offset")int offset);
}
