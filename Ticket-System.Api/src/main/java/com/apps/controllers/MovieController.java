package com.apps.controllers;


import com.apps.response.ResponseList;
import com.apps.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("movies")
    public ResponseEntity<?> findByLocationAndDate(@RequestParam(value = "location") Integer location,
                                                    @RequestParam(value = "date") String date){
        var resultList = movieService.findByLocationAndDate(location,date);
        var response = ResponseList.builder()
                .data(resultList)
                .total(resultList.size())
                .build();

        return ResponseEntity.ok(response);
    }
}
