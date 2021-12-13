package com.apps.service;

import com.apps.domain.entity.Member;
import com.apps.mapper.MemberDto;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface MemberService {

    List<MemberDto> findAll(Integer limit,Integer offset,
                            String sort,  String order,
                            Integer user,String startDate,
                            String endDate,String creationDate,
                            String level,String birthDay,
                            String cmnd, Boolean profile);

    int findAllCount(Integer user,String startDate,
                     String endDate,String creationDate,
                      String level,String birthDay,
                     String cmnd,Boolean profile);

    int update(MemberDto memberDto);

    int delete(Integer id);

    MemberDto findById(Integer id);

    MemberDto findByNumber(String number);


    int insert(MemberDto memberDto) throws SQLException;

    String checkLevelMember(double point);
}
