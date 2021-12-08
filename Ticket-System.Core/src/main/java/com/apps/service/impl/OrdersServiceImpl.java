package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.OrderStatus;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Concession;
import com.apps.domain.entity.OrderRoomDto;
import com.apps.domain.entity.Orders;
import com.apps.domain.entity.Seat;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.OrderDto;
import com.apps.mybatis.mysql.*;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.ConcessionMyOrder;
import com.apps.response.entity.MyOrderResponse;
import com.apps.response.entity.OrderSeats;
import com.apps.service.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    private final ShowTimesDetailService showTimesDetailService;
    private final TheaterService theaterService;
    private final LocationService locationService;
    private final SeatService seatService;


    @Autowired
    private KafkaTemplate<String, com.apps.config.kafka.Message> kafkaTemplate;
//
//    @Scheduled(fixedDelay = 1000)
//    public void sendMessage() throws ExecutionException, InterruptedException {
//        kafkaTemplate.send("test-websocket", new com.apps.config.kafka.Message("Notification","1","")).get();
//    }

    @Scheduled(fixedDelay = 100000)
    public void reportCurrentTime() throws ExecutionException, InterruptedException {
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
        if(this.userService.getUserFromContext() != 0){
            var listOrderNew = this.findAllOrderRoom(0,25,
                    "updatedAt","DESC",null,null,null,null,
                    null,Utilities.subDate(30));
            kafkaTemplate.send("test-websocket","order-chart",
                    new com.apps.config.kafka.Message("order",listOrderNew)).get();
        }
    }

    private List<Orders> addTotalToOrder(List<Orders> orders){
        for (var order:orders){
            if(!order.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
                order.setTotal(this.getTotalOrder(order));
            }
        }
        return orders;
    }

    private List<OrderRoomDto> filterOrderByTheater(List<OrderRoomDto> orders){
        return orders.stream().filter(order ->{
            var item = this.showTimesDetailService.findById(order.getShowTimesDetailId());
            return item.getTheaterId() == this.userService.getTheaterByUser();
        }).collect(Collectors.toList());
    }

    @Override
//    @Cacheable(value = "OrdersService" ,key = "'ListOrders'+#page +'-'+#size +'-'+#sort +'-'+#order +'-'+#showTimes+'-'+#order +'-'+#showTimes", unless = "#result == null")
    public List<Orders> findAll(int page, int size, String sort, String order, Integer showTimes,
                                String type, Integer userId,String status,Integer creation,String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        return this.addTotalToOrder(this.ordersRepository.findAll(size, page * size,sort,
                order,showTimes,type,userId,status,this.userService.isSeniorManager(idUserContext)
                        || this.userService.isManager(idUserContext) ? null : idUserContext,
                this.convertLocalDate(dateGte),null));
    }

    @Override
    public List<OrderRoomDto> findAllOrderRoom(int page, int size, String sort, String order,
                                               Integer showTimes, String type, Integer userId,
                                               String status, Integer creation, String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        var isSenior =  this.userService.isSeniorManager(idUserContext);
        var isManager= this.userService.isManager(idUserContext);
        return this.addInfoToOrders(this.findAll(page,size,sort,
                        order,showTimes,type,userId,status, isManager || isSenior ? null :
                        this.userService.getUserFromContext(), this.convertLocalDate(dateGte)),isManager,isSenior);
    }

    private List<OrderRoomDto> addInfoToOrders(List<Orders> orders,boolean isManager,boolean isSeniorManager){
        var ordersRoom = new ArrayList<OrderRoomDto>();
        for (var order: orders){
            var orderRoom = OrderRoomDto.builder()
                    .userId(order.getUserId()).createdDate(order.getCreatedDate())
                    .creation(order.getCreation()).expirePayment(order.getExpirePayment())
                    .id(order.getId()).tax(order.getTax()).profile(order.isProfile())
                    .note(order.getNote()).status(order.getStatus()).total(order.getTotal())
                    .updatedBy(order.getUpdatedBy()).updatedAt(order.getUpdatedAt())
                    .showTimesDetailId(order.getShowTimesDetailId())
                    .build();

            var showTimes = this.showTimesDetailService.findById(order.getShowTimesDetailId());
            orderRoom.setRoomName(showTimes.getRoomName());
            orderRoom.setTimeEnd(showTimes.getTimeEnd());
            orderRoom.setTimeStart(showTimes.getTimeStart());
            var theater = this.theaterService.findById(showTimes.getTheaterId());
            orderRoom.setTheaterName(theater.getName());
            var location  = this.locationService.findById(theater.getLocationId());
            orderRoom.setLocationName(location.getName());
            ordersRoom.add(orderRoom);
        }
        return isSeniorManager ? ordersRoom
                : isManager ? this.filterOrderByTheater(ordersRoom) : ordersRoom;
    }

    @Override
    public List<Orders> findCountAllOrderRoom(int page, int size, String sort, String order, Integer showTimes, String type, Integer userId, String status, Integer creation, String dateGte) {
        return null;
    }


    @Override
    public List<Orders> findAllMyOrders(int page, int size, String sort, String order,
                                        Integer showTimes, String type, String status, Integer creation,
                                        String dateGte,
            Boolean isYear) {
        return this.addTotalToOrder(this.ordersRepository.findMyOrders(size, page * size,
                sort,order,showTimes > 0 ? showTimes :null ,type,status, userService.getUserFromContext(),
                this.convertLocalDate(dateGte), isYear ? true : null));
    }

    private String convertLocalDate(String date){
        if(date != null && !date.isEmpty()){
            return Utilities.convertIsoToDate(date);
        }
        return null;
    }

    @Override
    public int findCountAllMyOrder(Integer showTimes, String type,
                                   String status, Integer creation,String dateGte,Boolean isYear) {
        return this.ordersRepository.findCountAllMyOrder(showTimes > 0 ? showTimes :null,
                type,status,creation > 0 ? creation : null,this.convertLocalDate(dateGte),isYear ? true : null);
    }

    @Override
    public int findAllCount(Integer showTimes, String type, Integer userId,
                            String status,Integer creation,String dateGte) {
        return this.ordersRepository.findCountAll(showTimes,type,userId,status,creation,this.convertLocalDate(dateGte),null);
    }

    private double getTotalOrder(Orders orders){
        var concessions = this.concessionRepository.findAllConcessionInOrder(orders.getId());
        var seats = this.ordersRepository.findSeatInOrders(orders.getId());
        double totalAmount = 0.d;
        for (var concession: concessions){
            totalAmount += concession.getPrice() * concession.getQuantity();
        }
        var showTime = this.showTimesDetailService.findById(orders.getShowTimesDetailId());
        for (var seat : seats){
            totalAmount += showTime.getPrice();
        }

        var taxAmount = (totalAmount / 100) * orders.getTax();
        return totalAmount + taxAmount;
    }

    private double getTotalOrder(List<ConcessionMyOrder> concessions, List<OrderSeats> seats,Orders orders){
        double totalAmount = 0.d;
        for (var concession: concessions){
            totalAmount += concession.getPrice() * concession.getQuantity();
        }
        var showTime = this.showTimesDetailService.findById(orders.getShowTimesDetailId());

        for (var seat : seats){
            totalAmount += showTime.getPrice();
        }
        var taxAmount = (totalAmount / 100) * orders.getTax();
        return totalAmount + taxAmount;
    }

    @Override
    public MyOrderResponse findById(int id) {

        Orders orders = this.ordersRepository.findById(id);
        if(orders == null){
            throw new NotFoundException("Not Find Object Have Id :" + id);
        }
        if(orders.getStatus().equals(OrderStatus.CANCELLED.getStatus())){

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
        String sql = "Insert into orders(user_id,showtimes_detail_id,created_date,note,creation,profile,status,expire_payment,total) values (?,?,?,?,?,?,?,?,?)";
        return this.ordersCustomRepository.insert(orders,sql);
    }

    @Override
    public int update(Orders orders) {
        return this.ordersRepository.update(orders);
    }

    private boolean isSeatsAvailable(Integer seat,List<Seat> idSeats){
        for(var item : idSeats){
            if(item.getId() == seat) return true;
        }
        return false;
    }

    @Override
    public int orderNonPayment(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException {
        var idSeats = this.seatService.findAllSeatInShowTimeUnavailable(orderDto.getShowTimesDetailId());
        for (var seat : orderDto.getSeats()){
            if(isSeatsAvailable(seat,idSeats)){
                throw new NotFoundException("Seat !" + seat + "reserved!");
            }
        }
        var taxAmount = (orderDto.getTotalAmount() / 100) * 10;
        Orders orders = Orders.builder()
                .creation(userService.getUserFromContext())
                .showTimesDetailId(orderDto.getShowTimesDetailId())
                .total(orderDto.getTotalAmount() + taxAmount)
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
            cacheManager.clearCache("");
            var listOrderNew = this.findAllOrderRoom(0,25,
                    "updatedAt","DESC",null,null,null,null,
                    null,Utilities.subDate(30));
            kafkaTemplate.send("test-websocket","order-chart",
                    new com.apps.config.kafka.Message("order",listOrderNew)).get();
            var showTimes = this.seatService.findShowTimesById(orderDto.getShowTimesDetailId());
            var seatMap = this.seatService.findByRoom(1,1000,"id","ASC",showTimes.getRoomId(),orderDto.getShowTimesDetailId());
            kafkaTemplate.send("test-websocket","seat-map",
                    new com.apps.config.kafka.Message("seat",seatMap)).get();
        }else{
            return 0;
        }
        return idOrderCreated;
    }



    @Override
    public int updateMyOrder(MyOrderUpdateDto orders) {

        if(orders.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
            var concessions = this.concessionRepository.findAllConcessionInOrder(orders.getId());
            var seats = this.ordersRepository.findSeatInOrders(orders.getId());
            double totalAmount = this.getTotalOrder(concessions,seats,this.ordersRepository.findById(orders.getId()));

            orders.setTotal(totalAmount);
            var listOrdersDetail = this.ordersRepository.findOrderDetailById(orders.getId());
            for (Integer orderDetail: listOrdersDetail){
                int deleted = this.ordersRepository.deleteOrderDetail(orderDetail);
            }
            var listOrdersSeat = this.ordersRepository.findOrderSeatById(orders.getId());
            for (Integer orderSeat: listOrdersSeat){
                int deleted = this.ordersRepository.deleteOrderSeat(orderSeat);
            }
        }
        return this.updateOrder(orders);
    }

    private int updateOrder(MyOrderUpdateDto orders){
        var order = new Orders();
        order.setId(orders.getId());
        order.setProfile(orders.getTypeUser());
        order.setUserId(orders.getTypeUser() ? orders.getUserId() : 0);
        order.setNote(orders.getNote());
        order.setStatus(orders.getStatus());
        order.setUpdatedAt(Utilities.getCurrentTime());
        order.setUpdatedBy(userService.getUserFromContext()) ;
        order.setTotal(orders.getTotal());
        return this.ordersRepository.updateMyOrder(order);
    }
}
