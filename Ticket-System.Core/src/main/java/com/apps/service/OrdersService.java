package com.apps.service;

import com.apps.domain.entity.Orders;
import com.apps.mapper.OrderDto;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.MyOrderResponse;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface OrdersService {
    List<Orders> findAll( int page, int size,
                         String sort,  String order,
                         Integer showTimes, String type,
                         Integer userId,String status,Integer creation);

    List<Orders> findAllMyOrders( int page, int size,
                          String sort,  String order,
                          Integer showTimes, String type,String status,Integer creation);

    int findCountAllMyOrder(Integer showTimes, String type, String status,
                            Integer creation);

    int findAllCount(Integer showTimes,String type,
                     Integer userId,String status,Integer creation);
    MyOrderResponse findById(int id);
    void delete(int id);
    int insert(Orders orders) throws SQLException;
    int update(Orders orders);
    int orderNonPayment(OrderDto orderDto) throws SQLException;
    int updateMyOrder(MyOrderUpdateDto orders);

}
