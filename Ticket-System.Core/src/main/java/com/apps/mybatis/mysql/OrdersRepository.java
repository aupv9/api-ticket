package com.apps.mybatis.mysql;


import com.apps.domain.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersRepository {

    List<Orders> findAll(@Param("limit") int limit, @Param("offset") int offset,
                         @Param("sort")String sort, @Param("order") String order,
                         @Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                         @Param("userId")Integer userId,@Param("status")String status
                        ,@Param("creation") Integer creation);

    List<Orders> findAllMyOrders(@Param("limit") int limit, @Param("offset") int offset,
                                 @Param("sort")String sort, @Param("order") String order,
                                 @Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                                 @Param("status")String status,@Param("creation") Integer creation);

    int findCountAll(@Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                     @Param("userId")Integer userId,@Param("status")String status
                    ,@Param("creation") Integer creation);

    int findCountAllMyOrder(@Param("showTimes")Integer showTimes, @Param("typeUser")String type
                            ,@Param("status")String status,@Param("creation") Integer creation);


    Orders findById(@Param("id")Integer id);

    @Delete("delete from orders where id = #{id}")
    void delete(@Param("id")Integer id);

    int update(@Param("entity") Orders orders);

    int insertOrderConcession(@Param("concessionId") int concessionId,
                              @Param("ordersId") int ordersId,
                              @Param("quantity") int quantity);
    int insertOrderSeat(@Param("seatId") int seatId,
                              @Param("ordersId") int ordersId);
}
