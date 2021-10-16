package com.apps.service;

import com.apps.domain.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

public interface OrdersService {
    List<Orders> findAll( int page, int size,
                         String sort,  String order,
                         Integer showTimes, String type,
                         Integer userId,String status);
    int findAllCount(Integer showTimes,String type,
                     Integer userId,String status);
    Orders findById(int id);
    void delete(int id);
    int insert(Orders orders) throws SQLException;
    int update(Orders orders);
}
