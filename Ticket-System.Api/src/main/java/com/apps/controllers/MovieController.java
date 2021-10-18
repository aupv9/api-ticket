package com.apps.controllers;


import com.apps.domain.entity.Movie;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseList;
import com.apps.response.ResponseRA;
import com.apps.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin("*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("movies-location")
    public ResponseEntity<?> findByLocationAndDate(@RequestParam(value = "location") Integer location,
                                                   @RequestParam(value = "date") String date) {
        var resultList = movieService.findByLocationAndDate(location, date);
        var response = ResponseList.builder()
                .data(resultList)
                .total(resultList.size())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("movies")
    public ResponseEntity<?> getMovies(@RequestParam(value = "pageSize", required = false) Integer size,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "sort", required = false) String sort,
                                       @RequestParam(value = "order", required = false) String order,
                                       @RequestParam(value = "search", required = false) String search) {
        var resultList = this.movieService.findAll(page - 1, size, search, sort, order);
        var totalElements = this.movieService.findAllCount(search);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("movies-theater")
//    public ResponseEntity<?> getMovies(@RequestParam(value = "pageSize", required = false) Integer size) {
//        var resultList = this.movieService.findAll(page - 1, size, search, sort, order);
//        var totalElements = this.movieService.findAllCount(search);
//        var response = ResponseRA.builder()
//                .content(resultList)
//                .totalElements(totalElements)
//                .build();
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable(value = "id", required = false) Integer id) {
        if(id == null) return ResponseEntity.ok(null);
        var result = this.movieService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("movies")
    public ResponseEntity<?> getMovieById(@RequestBody Movie movie) throws SQLException {
        var result = this.movieService.insert(movie);
        return ResponseEntity.ok(result);
    }

    @PutMapping("movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable("id") Integer id,@RequestBody Movie movie) throws SQLException {
        movie.setId(id);
        var resultUpdate = this.movieService.update(movie);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(movie)
                .build();
        return ResponseEntity.ok(response);
    }

    @Delete("movies/{id}")
    public ResponseEntity<?>  deleteMovieById(@PathVariable("id") Integer id) {
         this.movieService.delete(id);

        return ResponseEntity.ok(id);
    }
}