package com.apps.controllers;

import com.apps.response.ResponseRA;
import com.apps.service.ShowTimesDetailService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class TicketController {

    private final ShowTimesDetailService showTimesDetailService;

    public TicketController(ShowTimesDetailService showTimesDetailService) {
        this.showTimesDetailService = showTimesDetailService;
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
}
