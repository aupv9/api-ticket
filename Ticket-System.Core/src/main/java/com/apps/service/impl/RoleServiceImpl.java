package com.apps.service.impl;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.RoleRepository;
import com.apps.service.RoleService;
import lombok.var;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleById(Integer id) {
        return this.roleRepository.findRoleById(id);
    }

    @Override
    public List<Role> findAllRole(int limit,int offset,String sort,String order,Integer roleId,String search) {
        return this.roleRepository.findAll(limit, offset,sort,order,roleId,search);
    }

    @Override
    public int findAllCountRole(Integer roleId) {
        return this.roleRepository.findAllCountRole(roleId);
    }

    @Override
    public List<UserRole> findUserRoleById(Integer userId) {
        return this.roleRepository.findUserRoleById(userId);
    }

    @Override
    public List<Privilege> findAllPrivilege(int limit, int offset, String sort, String order, String search) {
        return this.roleRepository.findAllPrivilege(limit,offset,sort,order,search);
    }

    @Override
    public int findAllCountPrivilege(String search) {
        return this.roleRepository.findAllCountPrivilege(search);
    }

    @Override
    public List<RolePrivileges> findPrivilegesByRole(Integer roleId) {
        return this.roleRepository.findPrivilegesByRole(roleId);
    }

    @Override
    public List<Privilege> findAllPrivilegesByIdRole(Integer id) {
        var rolePrivileges = this.findPrivilegesByRole(id);
        List<Privilege> listPrivilege = new ArrayList<Privilege>();
        for (var privileges : rolePrivileges){
            var privilege = this.findPrivilegeById(privileges.getPrivilegeId());
            listPrivilege.add(privilege);
        }
        return listPrivilege;
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

    public Collection<? extends GrantedAuthority> getAuthorities(final List<UserRole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }
    private List<String> getPrivileges(final List<UserRole> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<RolePrivileges> collection = new ArrayList<>();
        for (final UserRole role : roles) {
            Role role1 = this.roleRepository.findRoleById(role.getRoleId());
            List<RolePrivileges> privilege = this.roleRepository.findPrivilegesByRole(role1.getId());
            collection.addAll(privilege);
        }
        for (final RolePrivileges item : collection) {
            Privilege privilege = this.roleRepository.findPrivilegeById(item.getPrivilegeId());
            privileges.add(privilege.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
