package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.OrderStatus;
import com.apps.domain.entity.Orders;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.OrderDto;
import com.apps.mybatis.mysql.OrdersRepository;
import com.apps.service.OrdersService;
import com.apps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersCustomRepository ordersCustomRepository;
    private final ApplicationCacheManager cacheManager;
    private final UserService userService;
    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersCustomRepository ordersCustomRepository, ApplicationCacheManager cacheManager, UserService userService) {
        this.ordersRepository = ordersRepository;
        this.ordersCustomRepository = ordersCustomRepository;
        this.cacheManager = cacheManager;
        this.userService = userService;
    }
    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {

    }


    @Override
    public List<Orders> findAll(int page, int size, String sort, String order, Integer showTimes,
                                String type, Integer userId,String status,Integer creation) {
        return this.ordersRepository.findAll(size, page * size,sort,order,showTimes,type,userId,status,creation);
    }

    @Override
    public List<Orders> findAllMyOrders(int page, int size, String sort, String order, Integer showTimes, String type, String status, Integer creation) {

        return  this.ordersRepository.findAllMyOrders(size, page * size,sort,order,showTimes > 0 ? showTimes :null ,type,status,userService.getUserFromContext());
    }

    @Override
    public int findCountAllMyOrder(Integer showTimes, String type, String status, Integer creation) {
        return this.ordersRepository.findCountAllMyOrder(showTimes > 0 ? showTimes :null,type,status,creation > 0 ? creation : null);
    }

    @Override
    public int findAllCount(Integer showTimes, String type, Integer userId,String status,Integer creation) {
        return this.ordersRepository.findCountAll(showTimes,type,userId,status,creation);
    }

    @Override
    public Orders findById(int id) {
        Orders orders = this.ordersRepository.findById(id);
        if(orders == null){
            throw new NotFoundException("Not Find Object Have Id :" + id);
        }
        return orders;
    }

    @Override
    public void delete(int id) {
        Orders orders = this.findById(id);
        this.ordersRepository.delete(orders.getId());
    }

    @Override
    public int insert(Orders orders) throws SQLException {
        String sql = "Insert into orders(user_id,showtimes_detail_id,tax,created_date,note,creation,non_profile,status) values (?,?,?,?,?,?,?,?)";
        return this.ordersCustomRepository.insert(orders,sql);
    }

    @Override
    public int update(Orders orders) {
        Orders orders1 = this.findById(orders.getId());
        orders1.setUserId(orders.getUserId());
        orders1.setShowTimesDetailId(orders.getShowTimesDetailId());
        orders1.setStatus(orders.getStatus());
        orders1.setTypeUser(orders.getTypeUser());
        orders1.setTax(orders.getTax());
        orders1.setCreation(orders.getCreation());
        orders1.setCreatedDate(orders.getCreatedDate());
        int result = this.ordersRepository.update(orders1);
        return result;
    }

    @Override
    public int orderNonPayment(OrderDto orderDto) throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.now();


        Orders orders = Orders.builder()
                .creation(userService.getUserFromContext())
                .showTimesDetailId(orderDto.getShowTimesDetailId())
//                .totalAmount(orderDto.getTotalAmount())
                .tax(0)
                .typeUser(0)
                .userId(orderDto.getUserId())
                .status(OrderStatus.NON_PAYMENT.getStatus())
                .userId(0)
                .createdDate(simpleDateFormat.format(localDateTime))
                .build();

        int idOrderCreated = this.insert(orders);
        if(idOrderCreated > 0){
            for (Integer seat : orderDto.getSeats()){
                this.ordersRepository.insertOrderSeat(seat,idOrderCreated);
            }
            Map<Integer, Integer> map = new HashMap<>();
            for (Integer concession : orderDto.getConcessionId()) {
                if(map.containsKey(concession)){
                    map.put(concession,map.get(concession) +  1);
                }else{
                    map.put(concession,1);
                }
            }
            for (Map.Entry<Integer,Integer> concession : map.entrySet() ){
                this.ordersRepository.insertOrderConcession(concession.getKey(),idOrderCreated,concession.getValue());
            }
        }else{
            return 0;
        }
        return idOrderCreated;
    }
}
