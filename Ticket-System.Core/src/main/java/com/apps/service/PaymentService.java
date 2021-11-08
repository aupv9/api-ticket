package com.apps.service;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;

import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    int insert(Payment payment);
    List<Payment> findAll(int limit, int offset,String sort,String order,String createdDate,String useFor,
                          String status,Integer creation,Integer method);
    int findAllCount(String createdDate,String useFor,String status,Integer creation,Integer method);
    List<PaymentMethod> findAllPaymentMethod();
    int findCountPaymentMethod();
    int insertReturnedId(Payment payment) throws SQLException;
    PaymentMethod findPaymentMethodById(int id);
    int deleteByOrder(int id);
    Payment findByOrder(int idOrder);
}
