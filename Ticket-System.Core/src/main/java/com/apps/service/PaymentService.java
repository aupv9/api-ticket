package com.apps.service;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import com.apps.mapper.PaymentDto;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PaymentService {
    int insert(Payment payment);
    List<Payment> findAll(Integer limit, Integer offset,String sort,String order,String createdDate,String useFor,
                          String status,Integer creation,Integer method);
    int findAllCount(String createdDate,String useFor,String status,Integer creation,Integer method);
    List<PaymentMethod> findAllPaymentMethod();
    int findCountPaymentMethod();
    int insertReturnedId(PaymentDto paymentDto) throws SQLException, ExecutionException, InterruptedException;
    PaymentMethod findPaymentMethodById(int id);
    int deleteByOrder(int id);
    Payment findByOrder(int idOrder);
    PaymentDto findById(int id);
    int update(PaymentDto paymentDto) throws ExecutionException, InterruptedException;
    List<Payment> findAllByDate(String date,Integer method);

    PaymentMethod findByCode(String code);
}
