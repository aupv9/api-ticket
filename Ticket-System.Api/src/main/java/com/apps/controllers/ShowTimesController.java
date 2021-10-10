package com.apps.controllers;

import com.apps.domain.entity.ShowTimes;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.ShowTimesService;
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
public class ShowTimesController {

    @Autowired
    private ShowTimesService showTimesService;

    @GetMapping("show-times")
    public ResponseEntity<?> getShowTimes(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order){
        var result  = showTimesService.findAll(page - 1, size, sort, order);
        var totalElement = showTimesService.findCountAll();
        var response = ResponseRA.builder()
                .content(result)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("show-times/{id}")
    public ResponseEntity<?> getShowTimes(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.showTimesService.findById(id));
    }

    @PostMapping(value = "showTimes",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody ShowTimes showTimes) throws SQLException {
        int id = this.showTimesService.insert(showTimes);
        showTimes.setId(id);
        return ResponseEntity.ok(showTimes);
    }

    @PutMapping(value = "showTimes/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateShowTimes(@PathVariable("id") Integer id ,
                                             @RequestBody ShowTimes showTimes) {
        showTimes.setId(id);
        int result = this.showTimesService.update(showTimes);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(showTimes)
                .build();
        return ResponseEntity.ok(response);
    }
}
