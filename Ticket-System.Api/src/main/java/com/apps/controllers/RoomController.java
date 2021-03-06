package com.apps.controllers;


import com.apps.domain.entity.Room;
import com.apps.domain.entity.UserAccountStatus;
import com.apps.request.RoomDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.response.ResponseStatus;
import com.apps.service.RoomService;
import com.apps.service.UserService;
import com.apps.service.impl.UserServiceImpl;
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
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("rooms")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false,defaultValue = "1") Integer size,
                                          @RequestParam(value = "page", required = false,defaultValue = "10")Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search", required = false) String search,
                                          @RequestParam(value = "theater_id", required = false,
                                                  defaultValue = "0") Integer theaterId
                                          ){
        var userId = this.userService.getUserFromContext();
        var isSeniorManager = this.userService.isSeniorManager(userId);
        if(theaterId == 0) theaterId = this.userService.getTheaterByUser();
        var resultList =
                this.roomService.findAll(page - 1,size,sort,order,search,theaterId,isSeniorManager);
        var totalElement = this.roomService.findCountAll(search,theaterId,isSeniorManager);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("rooms/{id}")
    public ResponseEntity<?> getLocations(@PathVariable(value = "id", required = false) Integer id){
        return ResponseEntity.ok(this.roomService.findById(id));
    }

    @PostMapping(value = "/rooms")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto room) throws SQLException {
        int id = this.roomService.insert(room);
        room.setId(id);
        return ResponseEntity.ok(room);
    }

    @PutMapping(value = "/rooms/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Integer id,@RequestBody RoomDto room) throws SQLException {

        room.setId(id);
        int result = this.roomService.update(room);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/rooms/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Integer id) throws SQLException {
        this.roomService.delete(id);
        return ResponseEntity.ok(id);
    }

}
