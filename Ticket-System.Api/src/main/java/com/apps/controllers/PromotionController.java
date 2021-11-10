package com.apps.controllers;

import com.apps.domain.entity.Payment;
import com.apps.response.ResponseRA;
import com.apps.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PromotionController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("payments")
    public ResponseEntity<?> getPayments(@RequestParam(value = "pageSize", required = false) Integer size,
                                         @RequestParam(value = "page", required = false)Integer page,
                                         @RequestParam(value = "sort", required = false) String sort,
                                         @RequestParam(value = "order", required = false) String order,
                                         @RequestParam(value = "created_date",  required = false) String createdDate,
                                         @RequestParam(value = "use_for",  required = false) String useFor,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "creation", required = false) Integer creation,
                                         @RequestParam(value = "method", required = false) Integer method){

        var resultList = this.paymentService.findAll(size, (page - 1 ) * size,sort,order,createdDate,
                useFor,status,creation,method);
        var totalElements = this.paymentService.findAllCount(createdDate,useFor,status,creation,method);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("payments")
    public ResponseEntity<?> createCategory(@RequestBody Payment payment) throws SQLException {
        int idReturned = this.paymentService.insertReturnedId(payment);
        payment.setId(idReturned);
        return ResponseEntity.ok(payment);
    }
}
