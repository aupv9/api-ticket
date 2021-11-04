package com.apps.service.impl;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public int insert(Payment payment) {
        return paymentRepository.insert(payment);
    }

    @Override
    public List<Payment> findAll(int page, int size) {
        return paymentRepository.findAll(size, page * size);
    }

    @Override
    public List<PaymentMethod> findAllPaymentMethod() {
        return this.paymentRepository.findAllPaymentMethod();
    }

    @Override
    public int findCountPaymentMethod() {
        return this.paymentRepository.findAllCountPaymentMethod();
    }
}
