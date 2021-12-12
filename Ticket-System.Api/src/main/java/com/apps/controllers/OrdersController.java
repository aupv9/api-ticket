package com.apps.controllers;

import com.apps.domain.entity.Orders;
import com.apps.mapper.OrderDto;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(value = "*")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("orders-room")
    public ResponseEntity<?> getOrderByRoom(@RequestParam(value = "pageSize", required = false) Integer size,
                                              @RequestParam(value = "page", required = false)Integer page,
                                              @RequestParam(value = "sort", required = false) String sort,
                                              @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "user_id",  required = false) Integer userId,
                                              @RequestParam(value = "type",  required = false) String type,
                                              @RequestParam(value = "showTimes_id", required = false) Integer showTimesId,
                                              @RequestParam(value = "status", required = false) String status,
                                              @RequestParam(value = "creation", required = false) Integer creation,
                                              @RequestParam(value = "date_gte", required = false) String dateGte
    ){
        var resultList = this.ordersService.findAllOrderRoom(size ,(page - 1) * size,sort,order,
                showTimesId,type,userId,status,creation,dateGte);

        var totalElements = resultList.size();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("orders")
    public ResponseEntity<?> getOrders(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "user_id",  required = false) Integer userId,
                                          @RequestParam(value = "type",  required = false) String type,
                                          @RequestParam(value = "showTimes_id", required = false) Integer showTimesId,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "creation", required = false) Integer creation,
                                          @RequestParam(value = "date_gte", required = false) String dateGte,
                                          @RequestHeader("Authorization") String token
                                          ){
        var resultList = this.ordersService.findAll(size,(page - 1) * size,sort,order,
                showTimesId,type,userId,status,creation,dateGte);
        var totalElements = this.ordersService.findAllOrderManagerCount(showTimesId,type,userId,status,creation,dateGte);
        var response = ResponseRA.builder()
                                    .content(resultList)
                                    .totalElements(totalElements)
                                    .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("my-orders")
    public ResponseEntity<?> getMyOrders( @RequestParam(value = "pageSize", required = false,defaultValue = "100") Integer size,
                                          @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "user_id",  required = false,defaultValue = "0") Integer userId,
                                          @RequestParam(value = "type",  required = false) String type,
                                          @RequestParam(value = "showTimes_id", required = false,defaultValue = "0") Integer showTimesId,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "creation", required = false,defaultValue = "0") Integer creation,
                                          @RequestParam(value = "date_gte", required = false) String dateGte,
                                          @RequestParam(value = "creation", required = false,defaultValue = "false") boolean isYear
                                          ){

        var resultList = this.ordersService.findAllMyOrders(page - 1 ,size,sort,order,showTimesId,
                type,status,creation,dateGte,isYear);
        var totalElements = this.ordersService.findCountAllMyOrder(showTimesId,type,status,
                creation,dateGte,isYear);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("my-orders/{id}")
    public ResponseEntity<?> getMyOrder(@PathVariable(value = "id", required = false) Integer id){
        var result = this.ordersService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "my-orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Integer id,
                                         @RequestBody MyOrderUpdateDto myOrderUpdateDto,
                                         @RequestHeader("Authorization")String token) throws ExecutionException, InterruptedException {

        myOrderUpdateDto.setId(id);
        int result = this.ordersService.updateMyOrder(myOrderUpdateDto);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<?> getCategory(@PathVariable(value = "id", required = false) Integer id){
        var result = this.ordersService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("ordersAnonymous")
//    @PreAuthorize("hasAuthority("")")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> ordersAnonymous(@RequestBody OrderDto orders) throws SQLException, ExecutionException, InterruptedException {
        int idReturned = this.ordersService.orderNonPaymentAnonymous(orders);
        orders.setId(idReturned);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("orders")
    public ResponseEntity<?> createCategory(@RequestBody OrderDto orders) throws SQLException, ExecutionException, InterruptedException {
        int idReturned = this.ordersService.orderNonPayment(orders);
        orders.setId(idReturned);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("orders/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer id,
                                             @RequestBody Orders orders,
                                             @RequestHeader("Authorization")String token){
        orders.setId(id);
        var resultUpdate = this.ordersService.update(orders);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(orders)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("orders/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer id){
        this.ordersService.delete(id);
        return ResponseEntity.ok(id);
    }

}
