package com.apps.service.impl;

import com.apps.domain.entity.Employee;
import com.apps.mybatis.mysql.EmployeeRepository;
import com.apps.service.EmployeeService;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll(Integer limit, Integer offset, String sort, String order, Integer roleId, Integer theaterId) {
        return this.employeeRepository.findAll(limit,offset,sort,order,roleId > 0 ? roleId : null,theaterId > 0 ? theaterId :null);
    }

    @Override
    public int findCountAll(Integer roleId, Integer theaterId) {
        return this.employeeRepository.findCountAll(roleId > 0 ? roleId : null,theaterId > 0 ? theaterId :null);
    }

    @Override
    public Employee findById(Integer id) {
        return this.employeeRepository.findById(id);
    }

    @Override
    public Employee findByUserId(Integer userId) {
        var empl = this.employeeRepository.findByUserId(userId);
        empl.setUserId(userId);
        return empl;
    }

    @Override
    public int insert(Integer userId, Integer roleId, Integer createdBy, String status) {
        return this.employeeRepository.insert(userId,roleId,createdBy,status);
    }

    @Override
    public int update(Employee employee) {
        return this.employeeRepository.update(employee);
    }
}
