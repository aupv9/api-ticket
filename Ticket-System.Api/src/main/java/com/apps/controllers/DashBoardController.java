package com.apps.controllers;

import com.apps.response.ResponseRA;
import com.apps.service.DashBoardService;
import com.apps.service.EmployeeService;
import com.apps.service.OrdersService;
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

    private final OrdersService ordersService;

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

    @GetMapping("percentCoverSeat")
    public ResponseEntity<?> getPercentCoverSeat(@RequestParam(value = "date", required = false) String date){

        var theaterId = this.userService.getTheaterByUser();
        var resultList = this.dashBoardService.getPercentCoverSeatOnTheater(date,theaterId);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("order-dashboard")
    public ResponseEntity<?> getOrderdashboard(@RequestParam(value = "date", required = false) String date){
        var resultList = this.ordersService.findOrderByDate(date);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("percentPaymentMethod")
    public ResponseEntity<?> percentPaymentMethod(@RequestParam(value = "date", required = false) String date){
        var resultList = this.dashBoardService.getPercentPaymentMethod(date);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("concessionRevenue")
    public ResponseEntity<?> concessionRevenue(@RequestParam(value = "startDate", required = false) String startDate,
                                               @RequestParam(value = "endDate", required = false) String endDate){
        var creation = this.userService.getUserFromContext();
        var resultList = this.dashBoardService.getRevenueConcession(startDate,endDate,creation);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("revenueMethod")
    public ResponseEntity<?> revenueMethod(@RequestParam(value = "startDate", required = false) String startDate,
                                               @RequestParam(value = "endDate", required = false) String endDate){
        var creation = this.userService.getUserFromContext();
        var resultList = this.dashBoardService.getRevenueMethod(startDate,endDate,creation);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(resultList.size())
                .build();
        return ResponseEntity.ok(response);
    }


}
