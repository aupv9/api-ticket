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
    public ResponseEntity<?> getRoles(){
        var resultList = this.roleService.findAllRole();
        var totalElements = this.roleService.findAllCountRole();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
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
