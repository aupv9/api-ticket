package com.apps.mybatis.mysql;

import com.apps.domain.entity.ShowTimesDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShowTimesDetailRepository {
    List<ShowTimesDetail> findAll(@Param("limit") int limit, @Param("offset") int offset);

    @Select("Select * from showtimes_detail where id = #{id}")
    ShowTimesDetail findById(int id);

    int insert(@Param("entity") ShowTimesDetail showTimesDetail);

}
