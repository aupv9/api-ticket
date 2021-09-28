package com.apps.mybatis.mysql;

import com.apps.domain.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentRepository {

    int insert(@Param("payment")Payment payment);

    List<Payment> findAll(@Param("limit") int limit, @Param("offset") int offset);
}
