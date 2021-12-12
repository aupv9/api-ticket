package com.apps.controllers;


import com.apps.domain.entity.Location;
import com.apps.domain.entity.Member;
import com.apps.mapper.MemberDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseCount;
import com.apps.response.ResponseRA;
import com.apps.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("members")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false,defaultValue = "25") Integer size,
                                          @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "user", required = false)Integer user,
                                          @RequestParam(value = "startDate", required = false)String startDate,
                                          @RequestParam(value = "endDate", required = false)String endDate,
                                          @RequestParam(value = "level", required = false)String level,
                                          @RequestParam(value = "birthDay", required = false)String birthDay,
                                          @RequestParam(value = "cmnd", required = false)String cmnd,
                                          @RequestParam(value = "cmnd", required = false,defaultValue = "false")Boolean profile,
                                          @RequestParam(value = "creationDate", required = false)String creationDate
                                          ){
        var resultList = this.memberService.findAll(size ,(page - 1) * size,sort,order,
                user,startDate,endDate,creationDate,level,birthDay,cmnd,profile);
        var totalElements = this.memberService.findAllCount(user,startDate,endDate,creationDate,
                level,birthDay,cmnd,profile);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("members/{id}")
    public ResponseEntity<?> getLocations(@PathVariable(value = "id", required = false) int idLocation){
        return ResponseEntity.ok(this.memberService.findById(idLocation));
    }


    @PostMapping("members")
    public ResponseEntity<?> createLocation(@RequestBody MemberDto member) throws SQLException {
        int idReturned = this.memberService.insert(member);
        member.setId(idReturned);
        return ResponseEntity.ok(member);
    }

    @PutMapping("members/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") int idMember,
                                             @RequestBody MemberDto memberDto){
        memberDto.setId(idMember);
        var resultUpdate = this.memberService.update(memberDto);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(memberDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("members/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer idMember){
        return ResponseEntity.ok(this.memberService.delete(idMember));
    }
}
