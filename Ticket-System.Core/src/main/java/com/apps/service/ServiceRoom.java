package com.apps.service;

import com.apps.domain.entity.Service;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface ServiceRoom {
    List<Service> findAll( Integer limit,Integer offset,
                           String sort,  String order,
                          String search);
    Service findById(Integer id);
    int update(Service service);
    int delete(Integer id);
    int insert(Service service) throws SQLException;
}
