package com.apps.controllers;


import com.apps.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("payments")
    public ResponseEntity<?> getLocations(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.paymentService.findAll(page,size));
    }




}
