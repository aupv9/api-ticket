package com.apps.controllers;


import com.apps.domain.entity.Room;
import com.apps.domain.entity.UserAccountStatus;
import com.apps.response.ResponseRA;
import com.apps.response.ResponseStatus;
import com.apps.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin("*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("rooms")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search", required = false) String search,
                                          @RequestParam(value = "theater_id",required = false) Integer theater){
        var resultList = this.roomService.findAll(page - 1,size,sort,order,search,theater);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/rooms",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createRoom(@RequestBody Room room) throws SQLException {
        int id = this.roomService.insert(room);
        return ResponseEntity.ok(room);
    }
}
