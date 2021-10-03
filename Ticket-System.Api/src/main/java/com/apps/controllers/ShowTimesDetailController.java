package com.apps.controllers;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.response.ResponseList;
import com.apps.service.ShowTimesDetailService;
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
public class ShowTimesDetailController {

    @Autowired
    private ShowTimesDetailService showTimesDetailService;

    @GetMapping("showTimesDetails")
    public ResponseEntity<?> showTimesDetails(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.showTimesDetailService.findAll(page,size));
    }

    @GetMapping("showTimesDetail/{id}")
    public ResponseEntity<?> showTimesDetail(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.showTimesDetailService.findById(id));
    }


    @GetMapping("showTimesDetail")
    public ResponseEntity<?> getLocations(@RequestParam("id") Integer id){
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




    @PostMapping(value = "showTimesDetail",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody ShowTimesDetail showTimes) throws SQLException {
        int id = this.showTimesDetailService.insert(showTimes);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }
}
