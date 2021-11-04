package com.apps.controllers;


import com.apps.response.ResponseRA;
import com.apps.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
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
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "created_date",  required = false) String createdDate,
                                          @RequestParam(value = "use_for",  required = false) String useFor,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "creation", required = false) Integer creation){

        var resultList = this.paymentService.findAllPaymentMethod();
        var totalElements = this.paymentService.findCountPaymentMethod();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("payments-method")
    public ResponseEntity<?> getPaymentMethod(){
        var resultList = this.paymentService.findAllPaymentMethod();
        var totalElements = this.paymentService.findCountPaymentMethod();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }





}
