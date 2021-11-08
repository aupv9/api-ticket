package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.OrderStatus;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Orders;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.OrderDto;
import com.apps.mybatis.mysql.ConcessionRepository;
import com.apps.mybatis.mysql.OrdersRepository;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.MyOrderResponse;
import com.apps.service.OrdersService;
import com.apps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
    private final ConcessionRepository concessionRepository;
    private final PaymentRepository paymentRepository;
    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersCustomRepository ordersCustomRepository, ApplicationCacheManager cacheManager, UserService userService, ConcessionRepository concessionRepository, PaymentRepository paymentRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersCustomRepository = ordersCustomRepository;
        this.cacheManager = cacheManager;
        this.userService = userService;
        this.concessionRepository = concessionRepository;
        this.paymentRepository = paymentRepository;
    }
    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    @Scheduled(fixedDelay = 100000)
    public void reportCurrentTime() {
        String currentTime = simpleDateFormat.format(LocalDateTime.now());
        var listOrderExpire = this.ordersRepository.findAllOrderExpiredReserved(currentTime);
        for (Integer order : listOrderExpire){
            var listOrdersDetail = this.ordersRepository.findOrderDetailById(order);
            for (Integer orderDetail: listOrdersDetail){
                int deleted = this.ordersRepository.deleteOrderDetail(orderDetail);
            }
            var listOrdersSeat = this.ordersRepository.findOrderSeatById(order);
            for (Integer orderSeat: listOrdersDetail){
                int deleted = this.ordersRepository.deleteOrderSeat(orderSeat);
            }
            int deleted = this.ordersRepository.delete(order);
        }
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
    public MyOrderResponse findById(int id) {

        Orders orders = this.ordersRepository.findById(id);
        if(orders == null){
            throw new NotFoundException("Not Find Object Have Id :" + id);
        }
        var concessions = this.concessionRepository.findAllConcessionInOrder(id);
        var seats = this.ordersRepository.findSeatInOrders(id);
        double totalAmount = 0.d;

        if(!orders.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
            for (var concession: concessions){
                totalAmount += concession.getPrice() * concession.getQuantity();
            }
            for (var seat : seats){
                totalAmount += seat.getPrice();
            }
        }else{
            var payment = this.paymentRepository.findByIdOrder(orders.getId());
            totalAmount = payment.getAmount();
        }


        return MyOrderResponse.builder()
                .id(id)
                .expirePayment(orders.getExpirePayment())
                .concessions(concessions)
                .seats(seats)
                .createdDate(orders.getCreatedDate())
                .updatedBy(orders.getUpdatedBy())
                .updatedDate(orders.getUpdatedAt())
                .tax(orders.getTax())
                .note(orders.getNote())
                .creation(orders.getCreation())
                .showTimesDetailId(orders.getShowTimesDetailId())
                .status(orders.getStatus())
                .typeUser(orders.isProfile())
                .userId(orders.getUserId())
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public void delete(int id) {
        var orders = this.findById(id);
        this.ordersRepository.delete(orders.getId());
    }

    @Override
    public int insert(Orders orders) throws SQLException {
        String sql = "Insert into orders(user_id,showtimes_detail_id,tax,created_date,note,creation,profile,status,expire_payment) values (?,?,?,?,?,?,?,?,?)";
        return this.ordersCustomRepository.insert(orders,sql);
    }

    @Override
    public int update(Orders orders) {
        var orders1 = this.findById(orders.getId());
//        orders.setUserId(orders.getUserId());
//        orders.setShowTimesDetailId(orders.getShowTimesDetailId());
//        orders.setStatus(orders.getStatus());
//        orders.setTypeUser(orders.getTypeUser());
//        orders.setTax(orders.getTax());
//        orders.setCreation(orders.getCreation());
//        orders.setCreatedDate(orders.getCreatedDate());
        orders.setUpdatedBy(userService.getUserFromContext());
        orders.setUpdatedAt(Utilities.getCurrentTime());
        int result = this.ordersRepository.update(orders);
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
                .profile(orderDto.getTypeUser())
                .userId(orderDto.getUserId())
                .status(OrderStatus.NON_PAYMENT.getStatus())
                .expirePayment(localDateTime.plusMinutes(5).format(simpleDateFormat))
                .userId(0)
                .createdDate(simpleDateFormat.format(localDateTime))
                .note("")
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

    @Override
    public int updateMyOrder(MyOrderUpdateDto orders) {
        if(orders.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
            var listOrdersDetail = this.ordersRepository.findOrderDetailById(orders.getId());
            for (Integer orderDetail: listOrdersDetail){
                int deleted = this.ordersRepository.deleteOrderDetail(orderDetail);
            }
            var listOrdersSeat = this.ordersRepository.findOrderSeatById(orders.getId());
            for (Integer orderSeat: listOrdersDetail){
                int deleted = this.ordersRepository.deleteOrderSeat(orderSeat);
            }
        }
        return this.updateOrder(orders);
    }

    private int updateOrder(MyOrderUpdateDto orders){
        var myOrder = this.findById(orders.getId());
        var order = Orders.builder()
                .id(myOrder.getId())
                .profile(orders.getTypeUser())
                .userId(orders.getTypeUser() ? orders.getUserId() : 0)
                .note(orders.getNote())
                .status(orders.getStatus())
                .updatedAt(Utilities.getCurrentTime())
                .updatedBy(userService.getUserFromContext())
                .build();
        return this.ordersRepository.updateMyOrder(order);
    }
}
