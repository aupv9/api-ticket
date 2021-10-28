package com.apps.controllers;

import com.apps.mapper.UserDto;
import com.apps.mapper.UserRegisterDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.UserService;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public ResponseEntity<?> findAllUser(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search",  required = false) String name,
                                          @RequestParam(value = "role", required = false) Integer role,
                                          @RequestParam(value = "status", required = false) String status
    ){
        if(status.equals("userRegister")){
            var resultList = this.userService.findAllUser(size,size * (page - 1),sort,order,name,role);
            var totalElements = this.userService.findCountAll(name,role);
            var response = ResponseRA.builder()
                    .content(Collections.singletonList(resultList))
                    .totalElements(totalElements)
                    .build();
            return ResponseEntity.ok(response);
        }else{
            var resultList = this.userService.findAllUserSocial(size,size * (page - 1),sort,order,name,role);
            var totalElements = this.userService.findCountAllSocial(name,role);
            var response = ResponseRA.builder()
                    .content(resultList)
                    .totalElements(totalElements)
                    .build();
            return ResponseEntity.ok(response);

        }
    }

    @PostMapping("users")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterDto userRegisterDto) throws SQLException {
        int idReturned = this.userService.registerAccountUser(userRegisterDto);
        userRegisterDto.setId(idReturned);
        return ResponseEntity.ok(userRegisterDto);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id",required = false) Integer id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    @PutMapping("users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id")Integer id,@RequestBody UserDto userDto) {
        userDto.setId(id);
        var resultUpdate = this.userService.update(userDto);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(userDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id",required = false) Integer id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }
}
