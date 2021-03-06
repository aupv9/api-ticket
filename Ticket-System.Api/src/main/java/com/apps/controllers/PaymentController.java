package com.apps.controllers;

import com.apps.domain.entity.Payment;
import com.apps.mapper.PaymentDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("payments")
    public ResponseEntity<?> getPayments( @RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "created_date",  required = false) String createdDate,
                                          @RequestParam(value = "use_for",  required = false) String useFor,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "creation", required = false,defaultValue = "0") Integer creation,
                                          @RequestParam(value = "method", required = false,defaultValue = "0") Integer method,
                                          @RequestHeader("Authorization") String token
    ){

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
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto) throws SQLException, ExecutionException, InterruptedException {
        int idReturned = this.paymentService.insertReturnedId(paymentDto);
        paymentDto.setId(idReturned);
        return ResponseEntity.ok(paymentDto);
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

    @GetMapping("paymentOrder/{id}")
    public ResponseEntity<?> getPaymentByOrder(@PathVariable("id")Integer id){
        return ResponseEntity.ok(this.paymentService.findByOrder(id));
    }

    @GetMapping("payments/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable("id")Integer id){
        return ResponseEntity.ok(this.paymentService.findById(id));
    }

    @GetMapping("payments-method/{id}")
    public ResponseEntity<?> getPaymentMethod(@PathVariable("id")Integer id){
        return ResponseEntity.ok(this.paymentService.findPaymentMethodById(id));
    }

    @PutMapping("payments/{id}")
    public ResponseEntity<?> getPaymentMethod(@PathVariable("id")Integer id,
                                              @RequestBody PaymentDto payment) throws ExecutionException, InterruptedException {
        payment.setId(id);
        var resultUpdate = this.paymentService.update(payment);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(payment)
                .build();
        return ResponseEntity.ok(response);
    }




}
