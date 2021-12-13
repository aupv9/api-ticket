package com.apps.mybatis.mysql;

import com.apps.domain.entity.Member;
import com.apps.mapper.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberRepository {

    List<MemberDto> findAll(@Param("limit")Integer limit,@Param("offset")Integer offset,
                            @Param("sort") String sort, @Param("order") String order,
                            @Param("user")Integer user,@Param("startDate")String startDate,
                            @Param("endDate")String endDate,@Param("creationDate")String creationDate,
                            @Param("level")String level,@Param("birthDay")String birthDay,
                            @Param("cmnd")String cmnd,@Param("profile")Boolean profile);

    int findAllCount( @Param("user")Integer user,@Param("startDate")String startDate,
                      @Param("endDate")String endDate,@Param("creationDate")String creationDate,
                      @Param("level")String level,@Param("birthDay")String birthDay,
                      @Param("cmnd")String cmnd,@Param("profile")Boolean profile);

    int update(@Param("entity")Member member);

    int delete(@Param("id")Integer id);

    MemberDto findById(@Param("id")Integer id);

    @Select("select * from membership where number = #{number}")
    MemberDto findByNumber(@Param("number")String number);

}
