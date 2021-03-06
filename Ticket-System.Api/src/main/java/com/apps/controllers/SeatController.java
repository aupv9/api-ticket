package com.apps.controllers;

import com.apps.domain.entity.Seat;
import com.apps.request.SeatDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
import com.apps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private UserService userService;


    @GetMapping("seats-validate")
    public ResponseEntity<?> getLocations( @RequestParam(value = "tier", required = false) String tier,
                                           @RequestParam(value = "numbers", required = false) Integer numbers,
                                           @RequestParam(value = "room_id",required = false) Integer room){
        var result  = seatService.validateSeat( room, tier, numbers);

        var response = ResponseRA.builder()
                .content(Collections.singletonList(result))
                .totalElements(result ? 1 : 0)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("seats")
    public ResponseEntity<?> getSeats(@RequestParam(value = "pageSize", required = false) Integer size,
                                      @RequestParam(value = "page", required = false)Integer page,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "search", required = false) String search,
                                      @RequestParam(value = "room_id",required = false) Integer room){
        var result  = seatService.findAll(page - 1, size, sort, order,  search,  room,this.userService.getUserFromContext());
        var totalElement = seatService.findCountAll(search,room,this.userService.getUserFromContext());
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("seatRoomAnonymous")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getSeatsByRoomOfAnonymous(@RequestParam(value = "pageSize", required = false,defaultValue = "100") Integer size,
                                                       @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                                       @RequestParam(value = "sort", required = false) String sort,
                                                       @RequestParam(value = "order", required = false) String order,
                                                       @RequestParam("showTimesId") Integer id,
                                                       @RequestParam("room") Integer room){
        var result  = seatService.findByRoom(size,(page - 1) * size,sort,order,room,id);
        var totalElement = seatService.findCountAll(null,room,this.userService.getUserFromContext());
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }
    private List<List<SeatDto>> seatToSeat(List<SeatDto> list) {
        Map<String,List<SeatDto>> map = new HashMap<>();
        for (var item:list){
            var key = item.getTier();
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
                map.get(key).add(item);
            }else{
                map.get(key).add(item);
            }
        }
        var listResult = new ArrayList<List<SeatDto>>();
        for (var item: map.entrySet()){
            listResult.add(item.getValue());
        }
        return listResult;
    }
    @GetMapping("getSeatsByShowTime")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getShowTime(@RequestParam(value = "date",required = false) String date,
                                         @RequestParam(value = "time",required = false) String time,
                                         @RequestParam(value = "theater",required = false) Integer theater,
                                         @RequestParam(value = "movie",required = false)Integer movie){

        var result  = this.seatToSeat(this.seatService.findByTheater(date,time,theater,movie));
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(result.size())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("seats-room")
    public ResponseEntity<?> getSeatByRoom(@RequestParam(value = "pageSize", required = false,defaultValue = "100") Integer size,
                                           @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                           @RequestParam(value = "sort", required = false) String sort,
                                           @RequestParam(value = "order", required = false) String order,
                                           @RequestParam("showTimesId") Integer id,
                                           @RequestParam("room") Integer room){
        var result  = seatService.findByRoom(size,(page - 1)*size,sort,order,room,id);
        var totalElement = seatService.findCountAll(null,room,this.userService.getUserFromContext());
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("seats-room-shows")
//    public ResponseEntity<?> getSeatByRoomShow(@RequestParam(value = "pageSize", required = false) Integer size,
//                                               @RequestParam(value = "page", required = false)Integer page,
//                                               @RequestParam(value = "sort", required = false) String sort,
//                                               @RequestParam(value = "order", required = false) String order,
//                                               @RequestParam("showTimesId") Integer id,
//                                               @RequestParam("room") Integer room){
//        var result  = seatService.findByRoomShow(,room,id);
//        var response = ResponseRA.builder()
//                .content(result)
//                .totalElements(result.size())
//                .build();
//        return ResponseEntity.ok(response);
//    }



    @GetMapping("seats/{id}")
    public ResponseEntity<?> getSeat(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.seatService.findById(id));
    }

    @DeleteMapping("seats/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable("id") Integer id){
        this.seatService.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("seats-AvaiableInRoom")
    public ResponseEntity<?> findSeatInRoomByShowTimesDetail(
                                          @RequestParam("showTimesId") Integer showTimesDetail,
                                          @RequestParam("room") Integer room){
        var resultList = this.seatService.findSeatInRoomByShowTimesDetail(showTimesDetail,room);
        var responseList = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping(value = "/seats",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createSeat(@RequestBody SeatDto seatDto) throws SQLException {
        int id = this.seatService.insert(seatDto);
        seatDto.setId(id);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(seatDto);
    }

    @PostMapping(value = "/seats-import",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createArraySeat(@RequestBody SeatDto seatDto) throws SQLException {
        int id = this.seatService.insert2(seatDto);
        seatDto.setId(id);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(seatDto);
    }

    @PutMapping(value = "/seats/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateSeat(@PathVariable("id") Integer id,@RequestBody Seat seat)  {
        seat.setId(id);
        int result = this.seatService.update(seat);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();
        return ResponseEntity.ok(response);
    }



}
