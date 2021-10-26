package com.apps.service.impl;

import com.apps.domain.entity.Employee;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.EmployeeRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.service.EmployeeService;
import lombok.var;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserAccountRepository userAccountRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserAccountRepository userAccountRepository) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
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
        Employee employee = this.employeeRepository.findById(id);
        if(employee == null){
            throw new NotFoundException("Not Found Employee :"+ id);
        }
        return employee;
    }

    @Override
    public Employee findByUserId(Integer userId) {
        var empl = this.employeeRepository.findByUserId(userId);
        if(empl == null){
            throw new NotFoundException("Not Found Employee :"+ userId);
        }
        empl.setUserId(userId);

        return empl;
    }

    @Override
    public int insert(Integer userId, Integer roleId, Integer createdBy, String status,String createdAt) {
        return this.employeeRepository.insert(userId,roleId,createdBy,status,createdAt);
    }

    @Override
    public int update(Employee employee) {
        System.out.println(employee);
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        Employee employee1 = this.findById(employee.getId());
        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails)authentication.getPrincipal();
        int modifiedBy = 0;
        if(userDetails != null){
            String email = userDetails.getUsername();
            modifiedBy = this.userAccountRepository.findUserByEmail(email).getId();
        }
        employee1.setStartsAt(employee.getStartsAt());
        employee1.setEndsAt(employee.getEndsAt());
        employee1.setUpdatedAt(localDateTime.format(simpleDateFormat));
        employee1.setStatus(employee.getStatus());
        employee1.setTheaterId(employee.getTheaterId());
        employee1.setNotes(employee.getNotes());
        employee1.setRoleId(employee.getRoleId());
        employee1.setUpdatedBy(modifiedBy);
        System.out.println(employee1);
        return this.employeeRepository.update(employee);
    }
}
