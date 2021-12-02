package com.apps.controllers;

import com.apps.domain.entity.Reserved;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
import com.apps.service.ShowTimesDetailService;
import com.apps.service.TicketService;
import com.apps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class TicketController {

    private final ShowTimesDetailService showTimesDetailService;
    private final SeatService seatService;
    private final TicketService ticketService;
    private final UserService userService;
    public TicketController(ShowTimesDetailService showTimesDetailService, SeatService seatService, TicketService ticketService, UserService userService) {
        this.showTimesDetailService = showTimesDetailService;
        this.seatService = seatService;
        this.ticketService = ticketService;
        this.userService = userService;
    }
    @GetMapping("shows")
    public ResponseEntity<?> getShowTimes(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "movie_id", required = false)Integer movieId,
                                          @RequestParam(value = "room_id", required = false) Integer roomId,
                                          @RequestParam(value = "search", required = false) String search,
                                          @RequestParam(value = "date_start", required = false) String dateStart,
                                          @RequestParam(value = "time_start", required = false) String timeStart
    ){
        var result  = showTimesDetailService.findAllShow(page - 1, size, sort, order, movieId,roomId,timeStart,search,dateStart);
        var totalElement = showTimesDetailService.findCountAllShow(movieId,roomId,timeStart,search,dateStart);
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("shows/{id}")
    public ResponseEntity<?> showTimesDetail(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.showTimesDetailService.findById(id));
    }

    @GetMapping("reserved")
    public ResponseEntity<?> reservedSeat(@RequestParam(value = "seat", required = false) Integer seat,
                                          @RequestParam(value = "showtime", required = false)Integer showtime,
                                          @RequestParam(value = "room", required = false) Integer room){
        var result = this.ticketService.findSeatReserved(seat,showtime,room);
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(result != null ? 1 : 0)
                .build();
        return ResponseEntity.ok(response);
    }

//    @PutMapping("reserved/{id}")
//    public ResponseEntity<?> reservedSeat(@PathVariable("id")Integer id,
//                                          @RequestBody Reserved reserved){
//
//        int result = this.ticketService.reserved(reserved.getSeats(), reserved.getUser(),
//                reserved.getShowTime(),reserved.getRoom());
//        reserved.setUser(userService.getUserFromContext());
//        var response = RAResponseUpdate.builder()
//                .id(result)
//                .previousData(reserved)
//                .data(reserved)
//                .build();
//        return ResponseEntity.ok(response);
//    }




//    @GetMapping("shows/{id}")
//    public ResponseEntity<?> getSeatByRoom(@PathVariable("id") Integer id){
//        var result  = seatService.findByShowTimes(id);
//        if(result != null){
//            var response = ResponseRA.builder()
//                    .content(result)
//                    .totalElements(result.size())
//                    .build();
//            return ResponseEntity.ok(response);
//        }
//        var response = ResponseRA.builder()
//                .content(new ArrayList<>())
//                .totalElements(0)
//                .build();
//        return ResponseEntity.ok(response);
//    }

}
