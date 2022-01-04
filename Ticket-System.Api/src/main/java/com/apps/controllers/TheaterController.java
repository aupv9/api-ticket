package com.apps.controllers;

import com.apps.domain.entity.Theater;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.TheaterService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin("*")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;



    @GetMapping("theaters")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getTheater(@RequestParam(value = "pageSize", required = false,defaultValue = "25") Integer size,
                                        @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "order", required = false) String order,
                                        @RequestParam(value = "search", required = false) String search,
                                        @RequestParam(value = "location_id",required = false) Integer location){

        var resultList = this.theaterService.findAll(page - 1,size,sort,order,search,location);
        var totalElement = this.theaterService.findCountAll(search,location);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }







    @GetMapping("theaters/{id}")
    public ResponseEntity<?> getLocations(@PathVariable(value = "id",required = false) Integer id){
        return ResponseEntity.ok(this.theaterService.findById(id));
    }

    @PutMapping(value = "/theaters/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@PathVariable("id")Integer idTheater,@RequestBody Theater theater) {
        theater.setId(idTheater);
        int id = this.theaterService.update(theater);
        var response = RAResponseUpdate.builder()
                .id(id)
                .previousData(theater)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("theaters/{id}")
    public ResponseEntity<?> deleteTheaters(@PathVariable(value = "id",required = false) Integer id){
        this.theaterService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @PostMapping("theaters")
    public ResponseEntity<?> createTheater(@RequestBody Theater theater){
        int id = this.theaterService.insert(theater);
        theater.setId(id);
        return ResponseEntity.ok(theater);
    }
}
