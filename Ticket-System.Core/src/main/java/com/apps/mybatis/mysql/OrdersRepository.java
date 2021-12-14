package com.apps.mybatis.mysql;

import com.apps.domain.entity.Orders;
import com.apps.payment.Order;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.OrderSeats;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersRepository {

    List<Orders> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                         @Param("sort")String sort, @Param("order") String order,
                         @Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                         @Param("userId")Integer userId,@Param("status")String status
                        ,@Param("creation") Integer creation,@Param("dateGte") String dateGte,
                         @Param("isYear")Boolean isYear);

    List<Orders> findMyOrders(@Param("limit") Integer limit, @Param("offset") Integer offset,
                                 @Param("sort")String sort, @Param("order") String order,
                                 @Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                                 @Param("status")String status,
                              @Param("creation") Integer creation,@Param("dateGte") String dateGte,
                              @Param("isYear")Boolean isYear);

    int findCountAll(@Param("showTimes")Integer showTimes, @Param("typeUser")String type,
                     @Param("userId")Integer userId,@Param("status")String status
                    ,@Param("creation") Integer creation,@Param("dateGte") String dateGte,
                     @Param("isYear")Boolean isYear);

    int findCountAllMyOrder(@Param("showTimes")Integer showTimes, @Param("typeUser")String type
                            ,@Param("status")String status,@Param("creation") Integer creation,@Param("dateGte") String dateGte,
                            @Param("isYear")Boolean isYear);


    Orders findById(@Param("id")Integer id);

    @Select("select * from orders where id = #{id}")
    Orders findOrderById(@Param("id")Integer id);

    @Delete("delete from orders where id = #{id}")
    int delete(@Param("id")Integer id);

    int update(@Param("order") Orders orders);

    int insertOrderConcession(@Param("concessionId") int concessionId,
                              @Param("ordersId") int ordersId,
                              @Param("quantity") int quantity);
    int insertOrderSeat(@Param("seatId") int seatId,
                              @Param("ordersId") int ordersId);

    List<OrderSeats> findSeatInOrders(@Param("id")Integer id);

    @Select("select id from orders where status = 'non_payment' and expire_payment < #{currentTime}")
    List<Integer> findAllOrderExpiredReserved(@Param("currentTime")String time);

    @Select("select orders_id from orders_detail where orders_id =#{ordersId}")
    List<Integer> findOrderDetailById(@Param("ordersId")Integer id);

    @Select("select orders_id from orders_seat where orders_id =#{ordersId}")
    List<Integer> findOrderSeatById(@Param("ordersId")Integer id);

    @Delete("delete from orders_detail where orders_id = #{ordersId}")
    int deleteOrderDetail(@Param("ordersId")Integer id);

    @Delete("delete from orders_seat where orders_id = #{ordersId}")
    int deleteOrderSeat(@Param("ordersId")Integer id);

    int updateMyOrder(@Param("order") Orders order);
}
