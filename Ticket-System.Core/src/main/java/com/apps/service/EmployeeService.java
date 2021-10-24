package com.apps.service;

import com.apps.domain.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll(Integer limit,Integer offset,
                          String sort, String order,
                          Integer roleId,Integer theaterId);
    int findCountAll(Integer roleId,Integer theaterId);
    Employee findById(Integer id);
    Employee findByUserId(Integer userId);
    int insert(Integer userId, Integer roleId,
               Integer createdBy, String status,String createdAt);
    int update(Employee employee);
}