package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.OrderStatus;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Concession;
import com.apps.domain.entity.Orders;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.OrderDto;
import com.apps.mybatis.mysql.ConcessionRepository;
import com.apps.mybatis.mysql.OrdersRepository;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.ConcessionMyOrder;
import com.apps.response.entity.MyOrderResponse;
import com.apps.response.entity.OrderSeats;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersCustomRepository ordersCustomRepository;
    private final ApplicationCacheManager cacheManager;
    private final UserService userService;
    private final ConcessionRepository concessionRepository;
    private final PaymentRepository paymentRepository;
    private final OfferHistoryService offerHistoryService;
    private final PromotionService promotionService;

    @Scheduled(fixedDelay = 100000)
    public void reportCurrentTime() {
        String currentTime = Utilities.getCurrentTime();
        var listOrderExpire = this.ordersRepository.findAllOrderExpiredReserved(currentTime);
        for (Integer order : listOrderExpire){
            var listOrdersDetail = this.ordersRepository.findOrderDetailById(order);
            if(listOrdersDetail.size() > 0){
                for (Integer orderDetail: listOrdersDetail){
                    int deleted = this.ordersRepository.deleteOrderDetail(orderDetail);
                }
            }

            var listOrdersSeat = this.ordersRepository.findOrderSeatById(order);
            if(listOrdersSeat.size() > 0){
                for (Integer orderSeat: listOrdersSeat){
                    int deleted = this.ordersRepository.deleteOrderSeat(orderSeat);
                }
            }
            int deleted = this.ordersRepository.delete(order);

        }
    }

    private List<Orders> addTotalToOrder(List<Orders> orders){
        for (var order:orders){
            order.setTotal(this.getTotalOrder(order));
        }
        return orders;
    }

    @Override
    public List<Orders> findAll(int page, int size, String sort, String order, Integer showTimes,
                                String type, Integer userId,String status,Integer creation,String dateGte) {
        return this.addTotalToOrder(this.ordersRepository.findAll(size, page * size,sort,order,showTimes,type,userId,status,creation, this.convertLocalDate(dateGte)));
    }


    @Override
    public List<Orders> findAllMyOrders(int page, int size, String sort, String order, Integer showTimes, String type, String status, Integer creation,String dateGte) {
        return this.addTotalToOrder(this.ordersRepository.findMyOrders(size, page * size,sort,order,showTimes > 0 ? showTimes :null ,type,status,userService.getUserFromContext(), this.convertLocalDate(dateGte)));
    }

    private String convertLocalDate(String date){
        if(date != null && !date.isEmpty()){
            return Utilities.convertIsoToDate(date);
        }
        return date;
    }
    @Override
    public int findCountAllMyOrder(Integer showTimes, String type, String status, Integer creation,String dateGte) {
        return this.ordersRepository.findCountAllMyOrder(showTimes > 0 ? showTimes :null,
                type,status,creation > 0 ? creation : null,this.convertLocalDate(dateGte));
    }

    @Override
    public int findAllCount(Integer showTimes, String type, Integer userId,String status,Integer creation,String dateGte) {
        return this.ordersRepository.findCountAll(showTimes,type,userId,status,creation,this.convertLocalDate(dateGte));
    }

    private double getTotalOrder(Orders orders){
        var concessions = this.concessionRepository.findAllConcessionInOrder(orders.getId());
        var seats = this.ordersRepository.findSeatInOrders(orders.getId());
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
        return totalAmount;
    }

    private double getTotalOrder(List<ConcessionMyOrder> concessions, List<OrderSeats> seats,Orders orders){
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
        return totalAmount;
    }

    @Override
    public MyOrderResponse findById(int id) {

        Orders orders = this.ordersRepository.findById(id);
        if(orders == null){
            throw new NotFoundException("Not Find Object Have Id :" + id);
        }
        var concessions = this.concessionRepository.findAllConcessionInOrder(id);
        var seats = this.ordersRepository.findSeatInOrders(id);
        double totalAmount = this.getTotalOrder(concessions,seats,orders);
        var response = MyOrderResponse.builder()
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
                .profile(orders.isProfile())
                .userId(orders.getUserId())
                .build();
        var offerHistory = this.offerHistoryService.findByOrder(id);
        if(offerHistory != null){
            var offer = this.promotionService.findById(offerHistory.getOfferId());
            var discountAmount = Utilities.getDiscountByCode(offerHistory.getCode(),totalAmount,offer);
            response.setDiscountAmount(discountAmount);
            response.setTotalAmount(totalAmount - response.getDiscountAmount());
        }
        return response;
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
        return this.ordersRepository.update(orders);
    }

    @Override
    public int orderNonPayment(OrderDto orderDto) throws SQLException {

        Orders orders = Orders.builder()
                .creation(userService.getUserFromContext())
                .showTimesDetailId(orderDto.getShowTimesDetailId())
//                .totalAmount(orderDto.getTotalAmount())
                .tax(10)
                .profile(orderDto.getTypeUser())
                .userId(orderDto.getUserId())
                .status(OrderStatus.NON_PAYMENT.getStatus())
                .expirePayment(Utilities.getTimeExpirePayment5m())
                .userId(0)
                .createdDate(Utilities.getCurrentTime())
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
        var order = new Orders();
        order.setId(myOrder.getId());
        order.setProfile(orders.getTypeUser());
        order.setUserId(orders.getTypeUser() ? orders.getUserId() : 0);
        order.setNote(orders.getNote());
        order.setStatus(orders.getStatus());
        order.setUpdatedAt(Utilities.getCurrentTime());
        order.setUpdatedBy(userService.getUserFromContext()) ;
        return this.ordersRepository.updateMyOrder(order);
    }
}
