package com.apps.controllers;


import com.apps.domain.entity.Theater;
import com.apps.service.TheaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @GetMapping("theaters")
    public ResponseEntity<?> getLocations(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.theaterService.findAll(page,size));
    }

    @GetMapping("theater/{id}")
    public ResponseEntity<?> getLocations(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.theaterService.findById(id));
    }

    @PostMapping(value = "/theater",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody Theater theater) {
        int id = this.theaterService.insert(theater);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }



}
