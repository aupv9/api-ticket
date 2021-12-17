package com.apps.controllers;


import com.apps.domain.entity.Movie;
import com.apps.request.MovieDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseList;
import com.apps.response.ResponseRA;
import com.apps.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("movies-showHaveOfWeek")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> findMovieHaveOfWeek() {
        var resultList = this.movieService.findAllCurrentWeek();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("movies-comingSoon")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> findMovieComingSoon() {
        var resultList = this.movieService.findAllComingSoon();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("movies")
    @PreAuthorize("permitAll()")
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
    public ResponseEntity<?> getMovieById(@RequestBody @Valid MovieDto movie) throws SQLException {
        var result = this.movieService.insertMulti(movie);
        movie.setId(result);
        return ResponseEntity.ok(movie);
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


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}