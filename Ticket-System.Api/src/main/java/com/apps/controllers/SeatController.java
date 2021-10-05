package com.apps.controllers;

import com.apps.domain.entity.Room;
import com.apps.domain.entity.Seat;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.response.ResponseList;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
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
public class SeatController {

    @Autowired
    private SeatService seatService;


    @GetMapping("seats")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search", required = false) String search,
                                          @RequestParam(value = "room_id",required = false) Integer room){
        var result  = seatService.findAll(page - 1, size, sort, order,  search,  room);
        var totalElement = seatService.findCountAll(search,room);
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("seat/{id}")
    public ResponseEntity<?> getLocations(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.seatService.findById(id));
    }

    @GetMapping("seatsAvaiableWithShowTimesAndRoom")
    public ResponseEntity<?> findSeatInRoomByShowTimesDetail(
                                          @RequestParam("showTimesDetail") Integer showTimesDetail,
                                          @RequestParam("room") Integer room){
        var resultList = this.seatService.findSeatInRoomByShowTimesDetail(showTimesDetail,room);
        var responseList = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping(value = "/seat",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody Seat seat) throws SQLException {
//        com.apps.response.ResponseStatus status = new com.apps.response.ResponseStatus();
//        if(userAccountStatus == null){
//            status.setStatus(com.apps.response.ResponseStatus.StatusType.WARNING);
//            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
//            return ResponseEntity.ok(status);
//        }
//        status =
        int id = this.seatService.insert(seat);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }



}
