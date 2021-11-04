package com.apps.service;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;

import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    int insert(Payment payment);
    List<Payment> findAll(int limit, int offset,String sort,String order,String createdDate,String useFor,String status,Integer creation);
    int findAllCount(String createdDate,String useFor,String status,Integer creation);
    List<PaymentMethod> findAllPaymentMethod();
    int findCountPaymentMethod();
    int insertReturnedId(Payment payment) throws SQLException;
    PaymentMethod findPaymentMethodById(int id);
}
