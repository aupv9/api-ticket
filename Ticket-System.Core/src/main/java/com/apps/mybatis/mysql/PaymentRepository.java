package com.apps.mybatis.mysql;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface PaymentRepository {

    int insert(@Param("payment")Payment payment);

    List<Payment> findAll(@Param("limit") int limit, @Param("offset") int offset,
                          @Param("sort") String sort, @Param("order") String order,
                          @Param("createdDate") String createdDate,@Param("useFor") String useFor,
                          @Param("status") String status,@Param("creation") Integer creation);

    int findAllCount(@Param("createdDate") String createdDate,@Param("useFor") String useFor,
                     @Param("status") String status,@Param("creation") Integer creation);

    @Select("SELECT * FROM payment_method")
    List<PaymentMethod> findAllPaymentMethod();

    @Select("SELECT count(*) FROM payment_method")
    int findAllCountPaymentMethod();

    @Select("select * from payment_method where id = #{id}")
    PaymentMethod findPaymentMethodById(@Param("id") int id);


}
