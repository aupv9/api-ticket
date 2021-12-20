package com.apps.service;

import com.apps.domain.entity.Employee;
import com.apps.response.entity.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAll(Integer limit, Integer offset,
                              String sort, String order,
                              Integer roleId, Integer theaterId);

//    List<RevenueEmployee> findAllRevenue(Integer limit, Integer offset,
//                                         String sort, String order,
//                                         Integer roleId, Integer theaterId);

    int findCountAll(Integer roleId,Integer theaterId);
    EmployeeDto findById(Integer id);
    Employee findByUserId(Integer userId);
    int insert(Integer userId,
               Integer createdBy, String status,String createdAt);
    int update(Employee employee);
    int delete(int id);
}
