package com.apps.controllers;

import com.apps.response.ResponseRA;
import com.apps.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> getRole(@PathVariable(value = "id", required = false) Integer id){
        var result = this.roleService.findRoleById(id);
        return ResponseEntity.ok(result);
    }



//
//    @PostMapping("categories")
//    public ResponseEntity<?> createCategory(@RequestBody Category category) throws SQLException {
//        int idReturned = this.categoryService.insert(category);
//        category.setId(idReturned);
//        return ResponseEntity.ok(category);
//    }
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
