package com.apps.service.impl;

import com.apps.contants.MemberEnum;
import com.apps.domain.entity.Member;
import com.apps.domain.repository.MemberCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.MemberDto;
import com.apps.mybatis.mysql.MemberRepository;
import com.apps.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import xyz.downgoon.snowflake.Snowflake;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCustomRepository memberCustomRepository;

    @Override
    public List<MemberDto> findAll(Integer limit, Integer offset, String sort, String order,
                                   Integer user, String startDate, String endDate,
                                   String creationDate, String level, String birthDay,
                                   String cmnd, Boolean profile) {
        return this.memberRepository.findAll(limit,offset,sort,order,user,startDate,endDate,creationDate,
                level,birthDay,cmnd,profile);
    }

    @Override
    public int findAllCount(Integer user, String startDate, String endDate, String creationDate, String level, String birthDay, String cmnd, Boolean profile) {
        return this.memberRepository.findAllCount(user,startDate,endDate,creationDate,
                level,birthDay,cmnd,profile);
    }

    @Override
    public int update(MemberDto memberDto) {
        var mem = this.memberRepository.findById(memberDto.getId());

        if(mem == null) throw new NotFoundException("Membership not found");
        var member = Member.builder()
                .point(memberDto.getPoint()).level(memberDto.getLevel())
                .startDate(memberDto.getStartDate()).endDate(memberDto.getEndDate()).profile(memberDto.getProfile())
                .birthday(memberDto.getBirthday()).id(memberDto.getId())
                .build();
        return this.memberRepository.update(member);
    }

    @Override
    public int delete(Integer id) {
        return this.memberRepository.delete(id);
    }

    @Override
    public MemberDto findById(Integer id) {
        return this.memberRepository.findById(id);
    }

    @Override
    public int insert(MemberDto memberDto) throws SQLException {

        String sql = "insert into membership(number,user_id,creation_date,start_date," +
                "end_date,level,point,profile,cmnd,birthday) values(?,?,?,?,?,?,?,?,?,?)";
        Snowflake snowflake = new Snowflake(1, 1);
        var member = Member.builder()
                .number(String.valueOf(snowflake.nextId()))
                .userId(memberDto.getProfile() ? memberDto.getUserId() : 0)
                .creationDate(memberDto.getCreationDate()).startDate(memberDto.getStartDate())
                .endDate(memberDto.getEndDate()).level(MemberEnum.MEMBER.name()).point(0.d)
                .profile(memberDto.getProfile()).cmnd(memberDto.getCmnd()).birthday(memberDto.getBirthday())
                .build();
        return this.memberCustomRepository.insert(member,sql);
    }
}
