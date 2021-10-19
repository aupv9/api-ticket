package com.apps.controllers;

import com.apps.mapper.UserRegisterDto;
import com.apps.response.ResponseRA;
import com.apps.service.UserService;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search",  required = false) String name,
                                          @RequestParam(value = "role", required = false) Integer role
    ){
        var resultList = this.userService.findAllUser(size,size * (page - 1),sort,order,name,role);
        var totalElements = this.userService.findCountAll(name,role);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("users")
    public ResponseEntity<?> createCategory(@RequestBody UserRegisterDto userRegisterDto) throws SQLException {
        int idReturned = this.userService.registerAccountUser(userRegisterDto);
        userRegisterDto.setId(idReturned);
        return ResponseEntity.ok(userRegisterDto);
    }




}
