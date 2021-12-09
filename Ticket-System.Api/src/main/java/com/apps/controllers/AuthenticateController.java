package com.apps.controllers;



import com.apps.mapper.UserRegisterDto;
import com.apps.request.GoogleLoginRequest;
import com.apps.request.UpdateStatusLogin;
import com.apps.request.UserLoginDto;
import com.apps.response.RAResponseUpdate;
import com.apps.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
public class AuthenticateController {

    private final UserService userService;

    public AuthenticateController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("registerUser")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterDto userRegisterDto) throws SQLException {
        if(this.userService.checkEmailAlready(userRegisterDto.getEmail())){
            return ResponseEntity.badRequest().body("Email Already");
        }
        int idReturned = this.userService.registerAccountUser(userRegisterDto);
        userRegisterDto.setId(idReturned);
        return ResponseEntity.ok(userRegisterDto);
    }



    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDto userLoginDto) throws JOSEException {
        var response = this.userService.authenticate(userLoginDto.getEmail(),userLoginDto.getPassword());
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }



    @PostMapping("authenticate-social")
    public ResponseEntity<?> authenticateSocial(@RequestBody GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException {
        if(this.userService.checkEmailAlready(googleLoginRequest.getEmail()))  {
            return ResponseEntity.ok("Email Already");
        }
        var response = this.userService.authenticateWithGoogle(googleLoginRequest);
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("update-status-login/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id")Integer id,@RequestBody UpdateStatusLogin googleLoginRequest){
        System.out.println(googleLoginRequest);
        var result = this.userService.updateCurrentLoggedByEmail(googleLoginRequest.getEmail());
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
