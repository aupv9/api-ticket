package com.apps.mybatis.mysql;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.PaymentMethod;
import com.apps.mapper.PaymentDto;
import org.apache.ibatis.annotations.Delete;
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
                          @Param("createdDate") String createdDate,
                          @Param("useFor") String useFor,
                          @Param("status") String status,@Param("creation") Integer creation,
                          @Param("method") Integer method);

    int findAllCount(@Param("createdDate") String createdDate,@Param("useFor") String useFor,
                     @Param("status") String status,@Param("creation") Integer creation,@Param("method") Integer method);

    @Select("SELECT * FROM payment_method")
    List<PaymentMethod> findAllPaymentMethod();

    @Select("select * from payment_method where code = #{code}")
    PaymentMethod findByCode(@Param("code")String code);

    @Select("SELECT count(*) FROM payment_method")
    int findAllCountPaymentMethod();

    @Select("select * from payment_method where id = #{id}")
    PaymentMethod findPaymentMethodById(@Param("id") int id);

    @Select("select * from payment where part_id =#{id}")
    Payment findByIdOrder(@Param("id")Integer id);

    @Select("select * from payment where id =#{id}")
    PaymentDto findById(@Param("id")Integer id);

    @Delete("delete from payment where part_id = #{id}")
    int deleteByOrder(@Param("id")Integer id);

    int update(@Param("payment")PaymentDto payment);

     List<Payment> findAllByDate(@Param("createdDate") String date, @Param("method")Integer method);


}
