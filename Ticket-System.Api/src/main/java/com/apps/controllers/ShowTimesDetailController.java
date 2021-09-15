package com.apps.controllers;


import com.apps.domain.entity.ShowTimes;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.service.ShowTimesDetailService;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> getLocations(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.showTimesDetailService.findAll(page,size));
    }

    @GetMapping("showTimesDetail/{id}")
    public ResponseEntity<?> getLocations(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.showTimesDetailService.findById(id));
    }

    @PostMapping(value = "showTimesDetail",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody ShowTimesDetail showTimes) throws SQLException {
//        com.apps.response.ResponseStatus status = new com.apps.response.ResponseStatus();
//        if(userAccountStatus == null){
//            status.setStatus(com.apps.response.ResponseStatus.StatusType.WARNING);
//            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
//            return ResponseEntity.ok(status);
//        }
//        status =
        int id = this.showTimesDetailService.insert(showTimes);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }
}
