package com.apps.mybatis.mysql;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleRepository {

    int insertUserRole(@Param("userId")Integer userId,@Param("roleId")Integer roleId);
    int insertRolePrivilege(@Param("roleId")Integer roleId,@Param("privilegeId")Integer privilegeId);
    int updateRoleByUser(@Param("userId")Integer userId,@Param("roleId")Integer roleId
            ,@Param("newRoleId")Integer newRoleId);

    List<Role> findAll(@Param("limit") int limit,@Param("offset")int offset,@Param("sort")String sort,
                       @Param("order")String order,@Param("roleId") Integer roleId,@Param("search")String search);


    int findAllCountRole(@Param("roleId") Integer roleId);

    List<Privilege> findAllPrivilege(@Param("limit") int limit,@Param("offset")int offset,@Param("sort")String sort,
                                     @Param("order")String order,@Param("search")String search);
    int findAllCountPrivilege(@Param("search")String search);



    Role findRoleById(@Param("id")Integer id);

    @Select("select * from role where name = #{name}")
    Role findByName(@Param("name")String name);

    List<UserRole> findUserRoleById(@Param("userId")Integer userId);



    @Select("select * from user_role where role_id = #{roleId} and user_id = #{userId}")
    UserRole findUserRoleByRoleAndUser(@Param("roleId")Integer roleId,
                                       @Param("userId")Integer userId);

    List<RolePrivileges> findPrivilegesByRole(@Param("roleId")Integer roleId);

    List<Privilege> findPrivilegesById(@Param("id")Integer id);


    @Select("select * from privilege where id =  #{id}")
    Privilege findPrivilegeById(Integer id);


    @Delete("delete from privilege where id = #{id}")
    void deletePrivilege(@Param("id")Integer id);

    @Delete("delete from user_role where user_id = #{userId}")
    void deleteUserRoleByUser(@Param("userId")Integer userId);

    @Delete("delete from user_role where user_id = #{userId} and role_id = #{roleId}")
    void deleteUserRole(@Param("userId")Integer userId,@Param("roleId")Integer iroleIdd);

    @Delete("delete from roles_privileges where role_id = #{roleId} and privilege_id = #{privilegeId}")
    void deleteRolePrivilege(@Param("roleId")Integer roleId, @Param("privilegeId")Integer privilegeId);

    @Delete("delete from roles_privileges where role_id = #{roleId}")
    void deleteRolePrivilegeByRole(@Param("roleId")Integer roleId);

    @Insert("Insert into roles_privileges(role_id,privilege_id) values(#{roleId},#{privilegesId})")
    int insertRolePrivileges(@Param("roleId")Integer integer,@Param("privilegesId")Integer privilegesId);

    @Update("update role set name = #{role.name}, code = #{role.code} where id = #{role.id}")
    int updateRole(@Param("role") Role role);
}
