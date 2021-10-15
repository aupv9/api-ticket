package com.apps.controllers;

import com.apps.domain.entity.Concessions;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.CategoryService;
import com.apps.service.ConcessionsService;
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
public class FoodController {

    private final ConcessionsService concessionsService;

    public FoodController(ConcessionsService concessionsService) {
        this.concessionsService = concessionsService;
    }

    @Autowired
    private CategoryService categoryService;


    @GetMapping("foods")
    public ResponseEntity<?> getFoods(@RequestParam(value = "pageSize", required = false) Integer size,
                                      @RequestParam(value = "page", required = false)Integer page,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "name",  required = false) String name,
                                      @RequestParam(value = "price",  required = false) Double price,
                                      @RequestParam(value = "category_id", required = false) Integer categoryId){

        var resultList = this.concessionsService.findAll(size,page - 1 ,sort,order,name,price,categoryId);
        var totalElements = this.concessionsService.findCountAll(name,categoryId);
//        var result = categoryService.findAll(page - 1,size,sort,order,name,null);
//        var totalElements = categoryService.findCountAll(name,null);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("foods/{id}")
    public ResponseEntity<?> getCategory(@PathVariable(value = "id", required = false) Integer id){
        var result = this.concessionsService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("foods")
    public ResponseEntity<?> createCategory(@RequestBody Concessions concessions) throws SQLException {
        int idReturned = this.concessionsService.insert(concessions);
        concessions.setId(idReturned);
        return ResponseEntity.ok(concessions);
    }

    @PutMapping("foods/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer id,
                                             @RequestBody Concessions concessions){
        concessions.setId(id);
        var resultUpdate = this.concessionsService.update(concessions);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(concessions)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("foods/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer id){
        this.concessionsService.delete(id);
        return ResponseEntity.ok(id);
    }
}
