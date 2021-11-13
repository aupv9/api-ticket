package com.apps.controllers;

import com.apps.filter.JWTService;
import com.apps.request.ScheduleEmailRequest;
import com.nimbusds.jose.JOSEException;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

@RestController
public class AppController {

    @Autowired
    private JWTService jwtService;

    private final RestTemplate restTemplate;

    public AppController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    @ResponseBody
    public Object index() {
        var scheduleEmail = new ScheduleEmailRequest();
        scheduleEmail.setEmail("aupv96@gmail.com");
        scheduleEmail.setSubject("test email");
        scheduleEmail.setBody("Hello");
        var response = restTemplate.postForEntity("http://localhost:8081/api/v1/scheduleEmail",scheduleEmail,ScheduleEmailRequest.class);
        return response.getBody();
    }

    @GetMapping("/testCreateToken")
    @ResponseBody
    public String generateToken() throws JOSEException {
        return jwtService.generatorToken("");
    }

    @GetMapping("/verifyToken")
    @ResponseBody
    public Boolean verrifyToken(@RequestParam("token") String token) throws JOSEException, ParseException {
        return jwtService.verifyToken(token);
    }


}
