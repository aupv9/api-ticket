package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import com.apps.domain.entity.RolePrivileges;
import com.apps.domain.entity.UserRole;
import com.apps.domain.repository.PrivilegeRepository;
import com.apps.domain.repository.RoleCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.RoleRepository;
import com.apps.response.RoleDto;
import com.apps.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RoleCustomRepository roleCustomRepository;
    private final ApplicationCacheManager cacheManager;

    @Override
    @Cacheable(value = "RoleService" ,key = "'findRoleById_'+#id", unless = "#result == null")
    public RoleDto findRoleById(Integer id) {
        var role = this.roleRepository.findRoleById(id);
        var privileges = this.roleRepository.findPrivilegesByRole(role.getId());
        return RoleDto.builder()
                .id(role.getId()).name(role.getName()).code(role.getCode())
                .privileges(privileges.stream().map(RolePrivileges::getPrivilegeId).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllRole_'+#limit+'-'+#offset" +
            "+'-'+#sort+'-'+#order+'-'+#search+'-'+#roleId", unless = "#result == null")
    public List<Role> findAllRole(int limit,int offset,String sort,String order,Integer roleId,String search) {
        return this.roleRepository.findAll(limit, offset,sort,order,roleId,search);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllCountRole_'+#roleId", unless = "#result == null")
    public int findAllCountRole(Integer roleId) {
        return this.roleRepository.findAllCountRole(roleId);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findUserRoleById_'+#userId", unless = "#result == null")
    public List<UserRole> findUserRoleById(Integer userId) {
        return this.roleRepository.findUserRoleById(userId);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllPrivilege_'+#limit+'-'+#offset" +
            "+#sort+'-'+#order+'-'+#search", unless = "#result == null")
    public List<Privilege> findAllPrivilege(int limit, int offset, String sort, String order, String search) {
        return this.roleRepository.findAllPrivilege(limit,offset,sort,order,search);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllCountPrivilege_'+#search", unless = "#result == null")
    public int findAllCountPrivilege(String search) {
        return this.roleRepository.findAllCountPrivilege(search);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findPrivilegesByRole_'+#roleId", unless = "#result == null")
    public List<RolePrivileges> findPrivilegesByRole(Integer roleId) {
        return this.roleRepository.findPrivilegesByRole(roleId);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllPrivilegesByIdRole_'+#roleId", unless = "#result == null")
    public List<Privilege> findAllPrivilegesByIdRole(Integer roleId) {
        var rolePrivileges = this.findPrivilegesByRole(roleId);
        List<Privilege> listPrivilege = new ArrayList<Privilege>();
        for (var privileges : rolePrivileges){
            var privilege = this.findPrivilegeById(privileges.getPrivilegeId());
            listPrivilege.add(privilege);
        }
        return listPrivilege;
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findAllPrivilegesById_'+#roleId", unless = "#result == null")
    public List<Privilege> findAllPrivilegesById(Integer roleId) {
        return this.roleRepository.findPrivilegesById(roleId);
    }

    @Override
    @Cacheable(value = "RoleService" ,key = "'findPrivilegeById_'+#roleId", unless = "#result == null")
    public Privilege findPrivilegeById(Integer roleId) {
        return this.roleRepository.findPrivilegeById(roleId);
    }

    @Override
    @CacheEvict(value = "RoleService",key = "'findPrivilegeById_'+#roleId",allEntries = true)
    public void deletePrivilege(Integer roleId) {
        Privilege privilege = this.roleRepository.findPrivilegeById(roleId);
        if(privilege == null){
            throw new NotFoundException("Not found object have id :" +roleId);
        }
        this.roleRepository.deletePrivilege(privilege.getId());
    }

    @Override
    @CacheEvict(value = "RoleService",key = "'findUserRoleById_'+#userId",allEntries = true)
    public void deleteUserRole(Integer userId, Integer roleId) {
        this.roleRepository.deleteUserRole(userId,roleId);
    }

    @Override
    @CacheEvict(value = "RoleService",key = "'findPrivilegesByRole_'+#roleId",allEntries = true)
    public void deleteRolePrivilege(Integer roleId, Integer privilegeId) {
        this.roleRepository.deleteRolePrivilege(roleId,privilegeId);
    }

    @Override
    @CacheEvict(value = "RoleService",allEntries = true)
    public int update(RoleDto roleDto) {
        var role = Role.builder()
                .id(roleDto.getId()).name(roleDto.getName()).code(roleDto.getCode())
                .build();
        this.roleRepository.deleteRolePrivilegeByRole(roleDto.getId());
        for (var privileges : roleDto.getPrivileges()){
            this.roleRepository.insertRolePrivileges(roleDto.getId(),privileges);
        }
        return this.roleRepository.updateRole(role);
    }

    @Override
    @CacheEvict(value = "RoleService",allEntries = true)
    public int insertPrivilege(Privilege privilege) throws SQLException {
        String sql = "insert into privilege(name) values(?)";
        return this.privilegeRepository.insert(privilege,sql);
    }

    @Override
    @CacheEvict(value = "RoleService",allEntries = true)
    public int insertRole(RoleDto roleDto) throws SQLException {
        String sql = "insert into role(name,code) values(?,?)";
        var role = Role.builder()
                .name(roleDto.getName()).code(roleDto.getCode())
                .build();
        int idRole = this.roleCustomRepository.insert(role,sql);
        for (var privileges : roleDto.getPrivileges()){
            this.roleRepository.insertRolePrivileges(idRole,privileges);
        }
        return idRole;
    }

    @Override
    public int deleteRole(Integer idRole) {
        return 0;
    }

    @Override
    @Cacheable(value = "RoleService",key = "'findRoleByCode_'+#code",unless = "#result == null ")
    public Role findRoleByCode(String code) {
        return this.roleRepository.findByCode(code);
    }

    //    @Cacheable(value = "RoleService",key = "'getAuthorities_'+#roles")
    public Collection<? extends GrantedAuthority> getAuthorities(final List<UserRole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

//    @Cacheable(value = "RoleService",key = "'getPrivileges_'+#roles")
    public List<String> getPrivileges(final List<UserRole> roles) {
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

    @Cacheable(value = "RoleService",key = "'getGrantedAuthorities_'+#privileges")
    public List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
