package com.apps.mybatis.mysql;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleRepository {

    int insertUserRole(@Param("userId")Integer userId,@Param("roleId")Integer roleId);
    int insertRolePrivilege(@Param("roleId")Integer roleId,@Param("privilegeId")Integer privilegeId);
    int updateRoleByUser(@Param("userId")Integer userId,@Param("roleId")Integer roleId);

    List<Role> findAll();
    @Select("select count(*) from role")
    int findAllCountRole();

    Role findRoleById(@Param("id")Integer id);

    @Select("select * from role where name = #{name}")
    Role findByName(@Param("name")String name);

    List<UserRole> findUserRoleById(@Param("userId")Integer userId);
    List<RolePrivileges> findPrivilegesByRole(@Param("roleId")Integer roleId);
    List<Privilege> findPrivilegesById(@Param("id")Integer id);

    @Select("select * from privilege where id =  #{id}")
    Privilege findPrivilegeById(Integer id);


    @Delete("delete from privilege where id = #{id}")
    void deletePrivilege(@Param("id")Integer id);

    @Delete("delete from user_role where user_id = #{userId} and role_id = #{roleId}")
    void deleteUserRole(@Param("userId")Integer userId,@Param("roleId")Integer iroleIdd);

    @Delete("delete from roles_privileges where role_id = #{roleId} and privilege_id = #{privilegeId}")
    void deleteRolePrivilege(@Param("roleId")Integer roleId, @Param("privilegeId")Integer privilegeId);

}
