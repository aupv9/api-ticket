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
    List<Orders> findAll( Integer limit, Integer offset,
                         String sort,  String order,
                         Integer showTimes, String type,
                         Integer userId,String status,Integer creation,String dateGte);

    List<Orders> findAllOrderManager(Integer limit, Integer offset,
                          String sort,  String order,
                          Integer showTimes, String type,
                          Integer userId,String status,Integer creation,String dateGte);

    List<OrderRoomDto> findAllOrderRoom(Integer limit, Integer offset,
                                        String sort, String order,
                                        Integer showTimes, String type,
                                        Integer userId, String status, Integer creation, String dateGte);

    List<Orders> findCountAllOrderRoom( Integer page, Integer size,
                                       String sort,  String order,
                                       Integer showTimes, String type,
                                       Integer userId,String status,Integer creation,String dateGte);

    List<Orders> findAllMyOrders( Integer page, Integer size,
                          String sort,  String order,
                          Integer showTimes, String type,String status,
                                  Integer creation,String dateGte,Boolean isYear);

    int findCountAllMyOrder(Integer showTimes, String type, String status,
                            Integer creation,String dateGte,Boolean isYear);

    int findAllOrderManagerCount(Integer showTimes,String type,
                     Integer userId,String status,Integer creation,String dateGte);

    int findAllCount(Integer showTimes,String type,
                     Integer userId,String status,Integer creation,String dateGte);
    MyOrderResponse findById(int id);

    Orders findOrdersById(int id);
    void delete(int id);
    int insert(Orders orders) throws SQLException;
    int update(Orders orders);
    int orderNonPayment(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException;
    int orderNonPaymentAnonymous(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException;

    int updateMyOrder(MyOrderUpdateDto orders) throws ExecutionException, InterruptedException;
    void sendDataToClient() throws ExecutionException, InterruptedException;
}
