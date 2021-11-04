package com.apps.controllers;

import com.apps.contants.PaymentFor;
import com.apps.payment.Order;
import com.apps.payment.PaypalService;
import com.apps.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
public class PaypalController {

    @Autowired
    PaypalService service;

    @Autowired
    private PaymentService paymentService;


    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/pay")
    public String payment(@RequestBody Order order) {
        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId,
                                        @RequestParam("PayerID") String payerId) {

        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                com.apps.domain.entity.Payment payment1 = new com.apps.domain.entity.Payment();
                payment1.setPaymentMethodId(1);
                LocalDate date = LocalDate.now(); // Gets the current date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                String currentDate = date.format(formatter);
                payment1.setCreatedDate(currentDate);
                payment1.setUseFor(PaymentFor.TICKET.getValue());
                payment1.setTransactionId(payment.getId());
                payment1.setAmount(Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal()));
                payment1.setNote(payment.getTransactions().get(0).getDescription());
                payment1.setStatus(payment.getPayer().getStatus());
                paymentService.insert(payment1);
                return ResponseEntity.ok(payment.toJSON());
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("Fail");
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<?> getPayment(@PathVariable("id") String id) {
        try {
            Payment payment = service.getPayment(id);
            System.out.println(payment.toJSON());
            return ResponseEntity.ok(payment.getTransactions().get(0).toJSON());
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("Fail");
    }

}