package com.apps.controllers;


import com.apps.domain.entity.Employee;
import com.apps.domain.entity.Location;
import com.apps.mapper.UserRegisterDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("employees")
    public ResponseEntity<?> findAllEmployee(@RequestParam(value = "pageSize", required = false) Integer size,
                                             @RequestParam(value = "page", required = false)Integer page,
                                             @RequestParam(value = "sort", required = false) String sort,
                                             @RequestParam(value = "order", required = false) String order,
                                             @RequestParam(value = "theater_id",  required = false) Integer theaterId,
                                             @RequestParam(value = "role_id", required = false) Integer roleId){
        var resultList = this.employeeService.findAll(size,size * (page - 1),sort,order,roleId,theaterId);
        var totalElements = this.employeeService.findCountAll(roleId,theaterId);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("employees/{id}")
    public ResponseEntity<?> findEmployees( @PathVariable(value = "id") Integer id)  {
        return ResponseEntity.ok(this.employeeService.findById(id));
    }

    @PutMapping("employees/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") int id,@RequestBody Employee employee){
        employee.setId(id);
        var resultUpdate = this.employeeService.update(employee);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(employee)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("employees/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) int idLocation){
        return ResponseEntity.ok(this.employeeService.delete(idLocation));
    }
}
