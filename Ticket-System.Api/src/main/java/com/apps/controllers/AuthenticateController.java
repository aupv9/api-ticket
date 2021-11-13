package com.apps.controllers;



import com.apps.request.GoogleLoginRequest;
import com.apps.request.UserLoginDto;
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


    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDto userLoginDto) throws JOSEException {
        var response = this.userService.authenticate(userLoginDto.getEmail(),userLoginDto.getPassword());
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("authenticate-social")
    public ResponseEntity<?> authenticateSocial(@RequestBody GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException {
        if(this.userService.checkEmailAlready(googleLoginRequest.getEmail()))  {
            return ResponseEntity.badRequest().body("Email Already");
        }
        var response = this.userService.authenticateWithGoogle(googleLoginRequest);
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("")
    public ResponseEntity<?> updateStatus(@RequestBody GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException {
        if(this.userService.checkEmailAlready(googleLoginRequest.getEmail()))  {
            return ResponseEntity.badRequest().body("Email Already");
        }
        var response = this.userService.authenticateWithGoogle(googleLoginRequest);
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }
}
