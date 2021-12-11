package com.apps.controllers;

import com.apps.domain.entity.Category;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("categories")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "name",  required = false) String name,
                                          @RequestParam(value = "type",  required = false) String type
                                          ){
        var resultList = this.categoryService.findAll(page - 1 ,size,sort,order,name,type);
        var totalElements = this.categoryService.findCountAll(name,type);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable(value = "id", required = false) Integer id){
        var result = this.categoryService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("categories")
    public ResponseEntity<?> createCategory(@RequestBody Category category) throws SQLException {
        int idReturned = this.categoryService.insert(category);
        category.setId(idReturned);
        return ResponseEntity.ok(category);
    }

    @PutMapping("categories/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer idLocation,
                                             @RequestBody Category category){
        category.setId(idLocation);
        var resultUpdate = this.categoryService.update(category);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(category)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("categories/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer id){
        this.categoryService.delete(id);
        return ResponseEntity.ok(id);
    }
}
