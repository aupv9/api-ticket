package com.apps.controllers;



import com.apps.request.UserLoginDto;
import com.apps.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class AuthenticateController {

    private final UserService userService;

    public AuthenticateController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("authenticate")
    public ResponseEntity<?> createLocation(@RequestBody UserLoginDto userLoginDto) throws JOSEException {
        var response = this.userService.authenticate(userLoginDto.getEmail(),userLoginDto.getPassword());
        if(response.getToken() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

}
