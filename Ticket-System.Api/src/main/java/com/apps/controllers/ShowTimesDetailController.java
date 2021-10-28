package com.apps.controllers;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.response.ResponseCount;
import com.apps.response.ResponseList;
import com.apps.response.ResponseRA;
import com.apps.service.ShowTimesDetailService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin("*")
public class ShowTimesDetailController {

    @Autowired
    private ShowTimesDetailService showTimesDetailService;

    @GetMapping("showTimesDetails")
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
        var result  = showTimesDetailService.findAll(page - 1, size, sort, order,
                movieId,roomId,timeStart,search,dateStart);
        var totalElement = showTimesDetailService.findCountAll(movieId,roomId,timeStart,search,dateStart);
        var response = ResponseRA.builder()
                .content(Collections.singletonList(result))
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("showTimesDetails/{id}")
    public ResponseEntity<?> showTimesDetail(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.showTimesDetailService.findById(id));
    }

    @DeleteMapping("showTimesDetails/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        this.showTimesDetailService.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("showTimesDetails-showtimes/{id}")
    public ResponseEntity<?> getShowTimesDetails(@PathVariable("id") Integer id){
        var resultList = this.showTimesDetailService.findByShowTimes(id);
        var responseList = ResponseList.builder()
                .data(resultList)
                .total(resultList.size())
                .build();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("showTimesDetailMini")
    public ResponseEntity<?> findByLocationAndDate(@RequestParam(value = "location") Integer location,
                                                   @RequestParam(value = "date") String date){
        var resultList = this.showTimesDetailService.findShowTimesDetailByLocationAndDate(location,date);
        var response = ResponseList.builder()
                .data(resultList)
                .total(resultList.size())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "showTimesDetails/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateShowTimes(@PathVariable("id")Integer idShowTimes,
                                             @RequestBody ShowTimesDetail showTimes) throws SQLException {
        showTimes.setId(idShowTimes);
        int id = this.showTimesDetailService.update(showTimes);
        showTimes.setId(id);
        return ResponseEntity.ok(showTimes);
    }

    @PostMapping(value = "showTimesDetails",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody ShowTimesDetail showTimes) throws SQLException {
        Instant instant = Instant.parse(showTimes.getTimeStart());
        Timestamp timestamp = Timestamp.from(instant);
        showTimes.setTimeStart(timestamp.toString());
        int id = this.showTimesDetailService.insert(showTimes);
        showTimes.setId(id);
        return ResponseEntity.ok(showTimes);
    }

    @GetMapping("count-showTimesDetails/{id}")
    public ResponseEntity<?> getShowTimesDetailByShowTimes(@PathVariable("id") Integer id){
        var count = this.showTimesDetailService.countShowTimesDetailByShowTimes(id);
        var response = ResponseCount.builder().id(id)
                .count(count).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("timeShowTimes")
    public ResponseEntity<?> getShowTimes(){
        var result  = showTimesDetailService.getTimeStart();
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(result.size())
                .build();
        return ResponseEntity.ok(response);
    }
}
