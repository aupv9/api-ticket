package com.apps.mybatis.mysql;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentRepository {

    int insert(@Param("payment")Payment payment);

    List<Payment> findAll(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM payment_method")
    List<PaymentMethod> findAllPaymentMethod();

    @Select("SELECT count(*) FROM payment_method")
    int findAllCountPaymentMethod();

}
