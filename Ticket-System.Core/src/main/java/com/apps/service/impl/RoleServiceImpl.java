package com.apps.service.impl;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.RoleRepository;
import com.apps.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findById(Integer id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public List<UserRole> findUserRoleById(Integer userId) {
        return this.roleRepository.findUserRoleById(userId);
    }

    @Override
    public List<RolePrivileges> findPrivilegesByRole(Integer roleId) {
        return this.roleRepository.findPrivilegesByRole(roleId);
    }

    @Override
    public List<Privilege> findAllPrivilegesById(Integer id) {
        return this.roleRepository.findPrivilegesById(id);
    }

    @Override
    public Privilege findPrivilegeById(Integer id) {
        return this.roleRepository.findPrivilegeById(id);
    }

    @Override
    public void deletePrivilege(Integer id) {
        Privilege privilege = this.roleRepository.findPrivilegeById(id);
        if(privilege == null){
            throw new NotFoundException("Not found obejct have id :" +id);
        }
        this.roleRepository.deletePrivilege(privilege.getId());
    }

    @Override
    public void deleteUserRole(Integer userId, Integer roleId) {
        this.roleRepository.deleteUserRole(userId,roleId);
    }

    @Override
    public void deleteRolePrivilege(Integer roleId, Integer privilegeId) {
        this.roleRepository.deleteRolePrivilege(roleId,privilegeId);
    }
}
