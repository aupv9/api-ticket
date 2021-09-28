package com.apps.service;

import com.apps.domain.entity.Payment;

import java.util.List;

public interface PaymentService {
    int insert(Payment payment);
    List<Payment> findAll(int limit, int offset);
}
