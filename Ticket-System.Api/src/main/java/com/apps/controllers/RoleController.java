package com.apps.controllers;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Room;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.response.RoleDto;
import com.apps.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(value = "*")
public class RoleController {

    private final RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("roles")
    public ResponseEntity<?> getRoles(@RequestParam(value = "pageSize", required = false) Integer size,
                                      @RequestParam(value = "page", required = false)Integer page,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "roleId",required = false)Integer roleId,
                                      @RequestParam(value = "search",required = false)String search){
        var resultList = this.roleService.findAllRole(size, (page - 1) * size,sort,order,roleId,search);
        var totalElements = this.roleService.findAllCountRole(roleId);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("privileges")
    public ResponseEntity<?> getPrivileges(@RequestParam(value ="pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search",required = false)String search){
        var resultList = this.roleService.findAllPrivilege(size, (page - 1) * size,sort,order,search);
        var totalElements = this.roleService.findAllCountPrivilege(search);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("privilege-role")
    public ResponseEntity<?> getPrivilegesRole(@RequestParam(value = "id", required = false)Integer roleId){
        var resultList = this.roleService.findAllPrivilegesByIdRole(roleId);
        var totalElements = resultList.size();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("privileges/{id}")
    public ResponseEntity<?> getPrivilege(@PathVariable(value = "id", required = false) Integer id){
        var result = this.roleService.findPrivilegeById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("roles/{id}")
    public ResponseEntity<?> getRoles(@PathVariable(value = "id", required = false) Integer id){
        var result = this.roleService.findRoleById(id);
        return ResponseEntity.ok(result);
    }


    @PutMapping(value = "roles/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Integer id,
                                        @RequestBody RoleDto roleDto){
        roleDto.setId(id);
        int result = this.roleService.update(roleDto);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("privileges")
    public ResponseEntity<?> createPrivilege(@RequestBody Privilege privilege) throws SQLException {
        int idReturned = this.roleService.insertPrivilege(privilege);
        privilege.setId(idReturned);
        return ResponseEntity.ok(privilege);
    }

    @PostMapping("roles")
    public ResponseEntity<?> createRoles(@RequestBody RoleDto roleDto) throws SQLException {
        int idReturned = this.roleService.insertRole(roleDto);
        roleDto.setId(idReturned);
        return ResponseEntity.ok(roleDto);
    }

    @DeleteMapping("roles/{id}")
    public ResponseEntity<?> getRole(@PathVariable(value = "id", required = false) Integer id){
        var result = this.roleService.deleteRole(id);
        return ResponseEntity.ok(result);
    }
//
//    @PutMapping("categories/{id}")
//    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer idLocation,
//                                             @RequestBody Category category){
//        category.setId(idLocation);
//        var resultUpdate = this.categoryService.update(category);
//        var response = RAResponseUpdate.builder()
//                .id(resultUpdate)
//                .previousData(category)
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping ("categories/{id}")
//    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer id){
//        this.categoryService.delete(id);
//        return ResponseEntity.ok(id);
//    }


}
