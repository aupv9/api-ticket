package com.apps.service;

import com.apps.domain.entity.Orders;

import java.sql.SQLException;
import java.util.List;

public interface OrdersService {
    List<Orders> findAll( int page, int size,
                         String sort,  String order,
                         Integer showTimes, String type,
                         Integer userId,String status,Integer creation);
    int findAllCount(Integer showTimes,String type,
                     Integer userId,String status,Integer creation);
    Orders findById(int id);
    void delete(int id);
    int insert(Orders orders) throws SQLException;
    int update(Orders orders);
}
