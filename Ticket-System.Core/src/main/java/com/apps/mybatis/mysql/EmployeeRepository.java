package com.apps.mybatis.mysql;

import com.apps.domain.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeRepository {
    List<Employee> findAll(@Param("limit")Integer limit,@Param("offset")Integer offset,
                           @Param("sort")String sort,@Param("order")String order,
                           @Param("roleId")Integer roleId,@Param("theaterId")Integer theaterId);
    int findCountAll(@Param("roleId")Integer roleId,@Param("theaterId")Integer theaterId);

    Employee findById(@Param("id")Integer id);

    @Select("select id, role_id, theater_id, createdBy,\n" +
            " createdAt, status,updatedAt,startsAt, endsAt, notes from employee where user_id = #{id}")
    Employee findByUserId(@Param("id")Integer id);

    int insert(@Param("userId")Integer userId,@Param("roleId")Integer roleId,
               @Param("createdBy")Integer createBy,@Param("status")String status,
               @Param("createdAt")String createdAt);

    int update(@Param("employee")Employee employee);
}
