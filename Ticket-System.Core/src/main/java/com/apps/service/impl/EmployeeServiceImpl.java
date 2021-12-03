package com.apps.service.impl;

import com.apps.domain.entity.Employee;
import com.apps.domain.entity.UserRole;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.EmployeeRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.response.entity.EmployeeDto;
import com.apps.service.EmployeeService;
import com.apps.service.RoleService;
import com.apps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final RoleService roleService;

    private final UserAccountRepository userAccountRepository;

    @Override
    public List<EmployeeDto> findAll(Integer limit, Integer offset, String sort, String order, Integer roleId, Integer theaterId) {
        return addRole(this.employeeRepository.findAll(limit,offset,sort,order,roleId > 0 ? roleId : null,theaterId > 0 ? theaterId :null));
    }
    private List<EmployeeDto> addRole(List<Employee> employees){
        var listEmployee = new ArrayList<EmployeeDto>();
        employees.forEach(employee -> {
            var employeeDto = EmployeeDto.builder()
                    .id(employee.getId()).createdAt(employee.getCreatedAt())
                    .createdBy(employee.getCreatedBy()).endsAt(employee.getEndsAt())
                    .notes(employee.getNotes()).updatedAt(employee.getUpdatedAt())
                    .updatedBy(employee.getUpdatedBy()).theaterId(employee.getTheaterId())
                    .status(employee.getStatus()).userId(employee.getUserId())
                    .startsAt(employee.getStartsAt()).endsAt(employee.getEndsAt())
                    .build();
            employeeDto.setRoleIds(this.getRolesByUser(employee.getUserId()));
            listEmployee.add(employeeDto);
        });
        return listEmployee;
    }

    private List<Integer> getRolesByUser(int userId){
        return this.roleService.findUserRoleById(userId)
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }
    @Override
    public int findCountAll(Integer roleId, Integer theaterId) {
        return this.employeeRepository.findCountAll(roleId > 0 ? roleId : null,theaterId > 0 ? theaterId :null);
    }

    @Override
    public EmployeeDto findById(Integer id) {
        Employee employee = this.employeeRepository.findById(id);
        if(employee == null){
            throw new NotFoundException("Not Found Employee :"+ id);
        }
        var listRoles = this.roleService.findUserRoleById(employee.getUserId())
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());

        return EmployeeDto.builder()
                .id(employee.getId()).createdAt(employee.getCreatedAt())
                .createdBy(employee.getCreatedBy()).endsAt(employee.getEndsAt())
                .notes(employee.getNotes()).updatedAt(employee.getUpdatedAt())
                .updatedBy(employee.getUpdatedBy()).theaterId(employee.getTheaterId())
                .status(employee.getStatus()).userId(employee.getUserId())
                .startsAt(employee.getStartsAt()).endsAt(employee.getEndsAt())
                .roleIds(listRoles)
                .build();
    }

    @Override
    public Employee findByUserId(Integer userId) {
        return this.employeeRepository.findByUserId(userId);
    }

    @Override
    public int insert(Integer userId, Integer roleId, Integer createdBy, String status,String createdAt) {
        return this.employeeRepository.insert(userId,roleId,createdBy,status,createdAt);
    }
    public int getUserFromContext() {
        int userId = 0;
        var userDetails = this.getAuthenticationFromContext();
        if( userDetails != null){
            String email = userDetails.getUsername();
            var user = this.userAccountRepository.findUserByEmail(email);
            if( user!= null){
                userId = user.getId();
            }else{
                var userInfo1 = this.userAccountRepository.findUserInfoByEmail(email);
                if(userInfo1 != null){
                    userId = userInfo1.getId();
                }
            }
        }
        return userId;
    }
    public UserDetails getAuthenticationFromContext() {
        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if((UserDetails) authentication.getPrincipal() != null){
            var userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
        }
        return  (UserDetails)authentication.getPrincipal();
    }
    @Override
    public int update(Employee employee) {
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        EmployeeDto employee1 = this.findById(employee.getId());
        int modifiedBy = this.getUserFromContext();
        employee1.setStartsAt(employee.getStartsAt());
        employee1.setEndsAt(employee.getEndsAt());
        employee1.setUpdatedAt(localDateTime.format(simpleDateFormat));
        employee1.setStatus(employee.getStatus());
        employee1.setTheaterId(employee.getTheaterId());
        employee1.setNotes(employee.getNotes());
        employee1.setUpdatedBy(modifiedBy);
        return this.employeeRepository.update(employee);
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
