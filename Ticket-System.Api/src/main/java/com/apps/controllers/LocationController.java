package com.apps.controllers;


import com.apps.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(value = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("locations")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false) Integer page){
        return ResponseEntity.ok(this.locationService.findAll(page,size));
    }

    @GetMapping("location")
    public ResponseEntity<?> getLocations(@RequestParam(value = "id", required = false) Integer idLocation){
        return ResponseEntity.ok(this.locationService.findById(idLocation));
    }
}
