package com.apps.controllers;

import com.apps.domain.entity.Location;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseCount;
import com.apps.response.ResponseRA;
import com.apps.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(value = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("locations")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search",  required = false) String search){
        var resultList = this.locationService.findAll(page - 1 ,size,sort,order,search);
        var totalElements = this.locationService.findCountAll(search);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("locations/{id}")
    public ResponseEntity<?> getLocations(@PathVariable(value = "id", required = false) Integer idLocation){
        var resultLocation = this.locationService.findById(idLocation);
        return ResponseEntity.ok(resultLocation);
    }

    @GetMapping("location-theater/{id}")
    public ResponseEntity<?> getCountTheaterByLocation(@PathVariable(value = "id", required = false) Integer idLocation){
        var count = this.locationService.countTheaterByLocation(idLocation);
        var response = ResponseCount.builder().id(count != null ? count : 0).build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("locations")
    public ResponseEntity<?> createLocation(@RequestBody Location location) throws SQLException {
        int idReturned = this.locationService.insert(location);
        location.setId(idReturned);
        return ResponseEntity.ok(location);
    }

    @PutMapping("locations/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer idLocation,@RequestBody Location location){
        location.setId(idLocation);
        var resultUpdate = this.locationService.update(location);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(location)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("locations/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer idLocation){
        return ResponseEntity.ok(this.locationService.delete(idLocation));
    }
}
