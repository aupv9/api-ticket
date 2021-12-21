package com.apps.service;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import com.apps.response.RoleDto;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    RoleDto findRoleById(Integer id);
    List<Role> findAllRole(int limit,int offset,String sort,String order,Integer roleId,String search);
    int findAllCountRole( Integer roleId);
    List<UserRole> findUserRoleById(Integer userId);

    List<Privilege> findAllPrivilege(int limit,int offset,String sort,String order,String search);
    int findAllCountPrivilege(String search);


    List<RolePrivileges> findPrivilegesByRole(Integer roleId);

    List<Privilege> findAllPrivilegesByIdRole(Integer id);

    List<Privilege> findAllPrivilegesById(Integer id);


    Privilege findPrivilegeById(Integer id);

    void deletePrivilege(Integer id);

    void deleteUserRole(Integer userId,Integer iroleIdd);

    void deleteRolePrivilege(Integer roleId,Integer privilegeId);

    int update(RoleDto roleDto);

    int insertPrivilege(Privilege privilege) throws SQLException;

    int insertRole(RoleDto roleDto) throws SQLException;

    int deleteRole(Integer idRole);

    Role findRoleByCode(String code);
}
