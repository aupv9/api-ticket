package com.apps.service.impl;

import com.apps.contants.PaymentFor;
import com.apps.contants.PaymentStatus;
import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import com.apps.domain.repository.PaymentCustomRepository;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.service.PaymentService;
import com.apps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentCustomRepository paymentCustomRepository;

    @Autowired
    private UserService userService;

    @Override
    public int insert(Payment payment) {
        return paymentRepository.insert(payment);
    }

    @Override
    public List<Payment> findAll(int limit, int offset, String sort, String order, String createdDate, String useFor, String status, Integer creation) {
        return this.paymentRepository.findAll(limit,offset,sort,order,createdDate,useFor,status,creation > 0 ? creation : null);
    }

    @Override
    public int findAllCount(String createdDate, String useFor, String status, Integer creation) {
        return this.paymentRepository.findAllCount(createdDate,useFor,status,creation > 0 ? creation : null);
    }

    @Override
    public List<PaymentMethod> findAllPaymentMethod() {
        return this.paymentRepository.findAllPaymentMethod();
    }

    @Override
    public int findCountPaymentMethod() {
        return this.paymentRepository.findAllCountPaymentMethod();
    }

    @Override
    public int insertReturnedId(Payment payment) throws SQLException {
        payment.setStatus(PaymentStatus.Verified.getValue());
        String sql = "Insert into payment(part_id,payment_method_id," +
                "creation,status,transaction_id,created_date,amount,use_for," +
                "note,user_id) values(?,?,?,?,?,?,?,?,?,?)";
        payment.setCreation(userService.getUserFromContext());
        payment.setCreatedDate(userService.getNowDateTime());
        payment.setUseFor(payment.getUseFor().equals("Order") ? PaymentFor.TICKET.getValue() :
                payment.getUseFor().equals("Gift") ? PaymentFor.GIFT.getValue() : PaymentFor.MEMBER_CASH.getValue());
        return this.paymentCustomRepository.insert(payment,sql);
    }

    @Override
    public PaymentMethod findPaymentMethodById(int id) {
        return this.paymentRepository.findPaymentMethodById(id);
    }
}
