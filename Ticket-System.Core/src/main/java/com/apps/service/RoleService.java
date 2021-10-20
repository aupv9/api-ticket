package com.apps.service;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;


import java.util.List;

public interface RoleService {
    Role findRoleById(Integer id);
    List<Role> findAllRole();
    int findAllCountRole();
    List<UserRole> findUserRoleById(Integer userId);

    List<RolePrivileges> findPrivilegesByRole(Integer roleId);

    List<Privilege> findAllPrivilegesById(Integer id);

    Privilege findPrivilegeById(Integer id);

    void deletePrivilege(Integer id);

    void deleteUserRole(Integer userId,Integer iroleIdd);

    void deleteRolePrivilege(Integer roleId,Integer privilegeId);
}
