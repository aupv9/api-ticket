package com.apps.controllers;

import com.apps.domain.entity.SeatRoom;
import com.apps.service.SeatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class SeatRoomController {


    @Autowired
    private SeatRoomService roomService;

    @GetMapping("seatRooms")
    public ResponseEntity<?> seatRooms(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.roomService.findAll(page,size));
    }

    @GetMapping("seatRoomByAll")
    public ResponseEntity<?> seatRoomByAll(@RequestParam("roomId") Integer roomId,
                                          @RequestParam("seatId") Integer seatId,
                                          @RequestParam("tierId") Integer tierId,
                                          @RequestParam("showTimesDetailId") Integer showTimesDetailId){
        return ResponseEntity.ok(this.roomService.findByAll(roomId,showTimesDetailId,seatId,tierId));
    }


    @PostMapping(value = "/seatRoom",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createSeatRoom(@RequestBody SeatRoom seatRoom) throws SQLException {
//        com.apps.response.ResponseStatus status = new com.apps.response.ResponseStatus();
//        if(userAccountStatus == null){
//            status.setStatus(com.apps.response.ResponseStatus.StatusType.WARNING);
//            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
//            return ResponseEntity.ok(status);
//        }
//        status =
        int id = this.roomService.insert(seatRoom);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/reservedSeat")
    public ResponseEntity<?> reservedSeat(@RequestParam("id") int id,
                                          @RequestParam("status") boolean status) throws SQLException {
        return ResponseEntity.ok(this.roomService.reservedSeat(id,status));
    }
}
