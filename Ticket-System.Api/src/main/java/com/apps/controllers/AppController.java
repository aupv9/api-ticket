package com.apps.controllers;

import com.apps.config.security.jwt.JWTService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class AppController {

    @Autowired
    private JWTService jwtService;

    @GetMapping("/test")
    @ResponseBody
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/testCreateToken")
    @ResponseBody
    public String generateToken() throws JOSEException {
        return jwtService.generatorToken();
    }

    @GetMapping("/verifyToken")
    @ResponseBody
    public Boolean verrifyToken(@RequestParam("token") String token) throws JOSEException, ParseException {
        return jwtService.verifyToken(token);
    }


}
