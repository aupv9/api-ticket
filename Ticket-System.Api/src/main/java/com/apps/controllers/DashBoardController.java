package com.apps.controllers;

import com.apps.response.ResponseRA;
import com.apps.service.DashBoardService;
import com.apps.service.EmployeeService;
import com.apps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class DashBoardController {
    private final DashBoardService dashBoardService;

    private final UserService userService;

    private final EmployeeService employeeService;

    @GetMapping("employees-revenue")
    public ResponseEntity<?> findAllEmployeeRevenue(@RequestParam(value = "pageSize", required = false) Integer size,
                                                    @RequestParam(value = "page", required = false)Integer page,
                                                    @RequestParam(value = "sort", required = false) String sort,
                                                    @RequestParam(value = "order", required = false) String order,
                                                    @RequestParam(value = "role_id", required = false,defaultValue = "0") Integer roleId,
                                                    @RequestParam(value = "date", required = false) String date
    ){

        var theaterId = this.userService.getTheaterByUser();
        var resultList = this.dashBoardService.findAllRevenue(size,size * (page - 1),sort,order,roleId,theaterId,date);
        var totalElements = this.employeeService.findCountAll(roleId,theaterId);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }


}
