package com.apps.mybatis.mysql;

import com.apps.domain.entity.ShowTimes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShowTimesRepository {

    List<ShowTimes> findAll(@Param("limit") int limit,@Param("offset") int offset,
                            @Param("sort") String sort, @Param("order") String order);

    int findAllCount();

    int insert(ShowTimes showTimes);

    @Select("SELECT * FROM showtimes where id  = #{id}")
    ShowTimes findById(@Param("id") int id);

}
