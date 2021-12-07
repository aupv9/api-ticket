package com.apps.service;

import com.apps.domain.entity.OrderRoomDto;
import com.apps.domain.entity.Orders;
import com.apps.mapper.OrderDto;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.MyOrderResponse;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrdersService {
    List<Orders> findAll( int page, int size,
                         String sort,  String order,
                         Integer showTimes, String type,
                         Integer userId,String status,Integer creation,String dateGte);

    List<OrderRoomDto> findAllOrderRoom(int page, int size,
                                        String sort, String order,
                                        Integer showTimes, String type,
                                        Integer userId, String status, Integer creation, String dateGte);

    List<Orders> findCountAllOrderRoom( int page, int size,
                                       String sort,  String order,
                                       Integer showTimes, String type,
                                       Integer userId,String status,Integer creation,String dateGte);

    List<Orders> findAllMyOrders( int page, int size,
                          String sort,  String order,
                          Integer showTimes, String type,String status,
                                  Integer creation,String dateGte,Boolean isYear);

    int findCountAllMyOrder(Integer showTimes, String type, String status,
                            Integer creation,String dateGte,Boolean isYear);

    int findAllCount(Integer showTimes,String type,
                     Integer userId,String status,Integer creation,String dateGte);
    MyOrderResponse findById(int id);
    void delete(int id);
    int insert(Orders orders) throws SQLException;
    int update(Orders orders);
    int orderNonPayment(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException;
    int updateMyOrder(MyOrderUpdateDto orders);

}
