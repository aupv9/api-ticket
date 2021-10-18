package com.apps.controllers;

import com.apps.domain.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class UserController {



//    @PostMapping("users")
//    public ResponseEntity<?> createCategory(@RequestBody Category category) throws SQLException {
//        int idReturned = this.categoryService.insert(category);
//        category.setId(idReturned);
//        return ResponseEntity.ok(category);
//    }
}
