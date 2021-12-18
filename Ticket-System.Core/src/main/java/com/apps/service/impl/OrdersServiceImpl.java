package com.apps.service.impl;

import com.apps.contants.OrderStatus;
import com.apps.contants.ShowStatusEnum;
import com.apps.contants.Utilities;
import com.apps.domain.entity.OrderRoomDto;
import com.apps.domain.entity.Orders;
import com.apps.domain.entity.Seat;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mapper.OrderDto;
import com.apps.mapper.OrderStatistics;
import com.apps.mybatis.mysql.*;
import com.apps.request.MyOrderUpdateDto;
import com.apps.response.entity.ConcessionMyOrder;
import com.apps.response.entity.MyOrderResponse;
import com.apps.response.entity.OrderSeats;
import com.apps.service.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersCustomRepository ordersCustomRepository;
    private final UserService userService;
    private final ConcessionRepository concessionRepository;
    private final OfferHistoryService offerHistoryService;
    private final PromotionService promotionService;
    private final ShowTimesDetailServiceImpl showTimesDetailService;
    private final TheaterService theaterService;
    private final LocationService locationService;
    private final SeatService seatService;
    private final MovieService movieService;

    @Autowired
    private KafkaTemplate<String, com.apps.config.kafka.Message> kafkaTemplate;


    @Scheduled(fixedRate = 500000)
    public void reportCurrentTime() throws ExecutionException, InterruptedException {
        String currentTime = Utilities.getCurrentTime();
        var listOrderExpire = this.findAllOrderExpiredReserved(currentTime);
        for (Integer order : listOrderExpire){
            var listOrdersDetail = this.findOrderDetailById(order);
            if(listOrdersDetail.size() > 0){
                for (Integer orderDetail: listOrdersDetail){
                    int deleted = this.deleteOrderDetail(orderDetail);
                }
            }
            var listOrdersSeat = this.findOrderSeatById(order);
            if(listOrdersSeat.size() > 0){
                for (Integer orderSeat: listOrdersSeat){
                    int deleted = this.deleteOrderSeat(orderSeat);
                }
            }
            int deleted = this.delete(order);
        }
        if(this.userService.getUserFromContext() != 0){
            var listOrderNew = this.findAllOrderRoom(0,25,
                    "updatedAt","DESC",null,null,null,null,
                    null,Utilities.subDate(30));
            var listOrders =this.findAllOrderRoom(0,1000,
                    "updatedAt","DESC",null,null,null,
                    null,null,null);
            List<Object> objectList = new ArrayList<>();
            objectList.add(listOrderNew);
            objectList.add(listOrders);
            kafkaTemplate.send("test-websocket","order-chart",
                    new com.apps.config.kafka.Message("order",objectList)).get();
        }
    }


    @Scheduled(fixedRate = 2000)
    public void updateStatusShowTimes(){
        var listShowTimesNowPlaying =
                this.showTimesDetailService.findAllSeniorManager(null,null,"id","ASC",
                        0,0,null,null,0,true,false);
        listShowTimesNowPlaying.parallelStream().forEach(item ->{
                item.setStatus(ShowStatusEnum.Now.getName());
                this.showTimesDetailService.update(item);
        });

        var listShowTimesSoon =
                this.showTimesDetailService.findAllSeniorManager(null,null,"id","ASC",
                        0,0,null,null,0,false,true);
        listShowTimesSoon.parallelStream().forEach(item ->{
            item.setStatus(ShowStatusEnum.Soon.getName());
            this.showTimesDetailService.update(item);
        });

        var listShowTimes =
                this.showTimesDetailService.findAllSeniorManager(null,null,"id","ASC",
                        0,0,null,null,0,false,false);
        listShowTimes.parallelStream().forEach(item ->{
            var isExpire = Timestamp.valueOf(item.getTimeEnd()).compareTo(Timestamp.from(Instant.now()));
            if(isExpire == 0){
                item.setStatus(ShowStatusEnum.Expire.getName());
                this.showTimesDetailService.update(item);
            }
        });

    }



    private List<Orders> addTotalToOrder(List<Orders> orders){
        for (var order:orders){
            if(!order.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
                order.setTotal(this.getTotalOrder(order));
            }
        }
        return orders;
    }



    private List<OrderStatistics> addTotalToOrderStatistics(List<OrderStatistics> orders){
        for (var order:orders){
            if(!order.getStatus().equals(OrderStatus.CANCELLED.getStatus())){
                var orderTotal = this.getTotalOrder(order);
                order.setTotal(orderTotal.getTotal());
                order.setTotalSeats(orderTotal.getTotalSeats());
                order.setTotalConcessions(orderTotal.getTotalConcessions());
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
    public List<Integer> findAllOrderExpiredReserved(String time) {
        return this.ordersRepository.findAllOrderExpiredReserved(time);
    }

    @Override
    public List<Integer> findOrderDetailById(Integer id) {
        return this.ordersRepository.findOrderDetailById(id);
    }

    @Override
    public List<Integer> findOrderSeatById(Integer id) {
        return this.ordersRepository.findOrderSeatById(id);
    }

    @Override
    public int deleteOrderDetail(Integer id) {
        return this.ordersRepository.deleteOrderDetail(id);
    }

    @Override
    public int deleteOrderSeat(Integer id) {
        return this.ordersRepository.deleteOrderSeat(id);
    }

    @Override
    public int delete(Integer id) {
        return this.ordersRepository.delete(id);
    }

    @Override
    public List<Orders> findAll(Integer limit, Integer offset, String sort, String order, Integer showTimes,
                                String type, Integer userId,String status,Integer creation,String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        var isSenior  = this.userService.isSeniorManager(idUserContext);

        return isSenior ? this.addTotalToOrder(this.findAllOrders(limit, offset,sort, order,showTimes,type,userId,status,
                                    creation, this.convertLocalDate(dateGte),null)) :
                this.addTotalToOrder(this.findAllOrderManager(limit,offset,sort,order,showTimes,type,
                        userId,status, creation,
                        this.convertLocalDate(dateGte)));
    }

    @Override
    public List<Orders> findAllOrderManager(Integer limit, Integer offset, String sort, String order,
                                            Integer showTimes, String type, Integer userId, String status,
                                            Integer creation, String dateGte) {

        return this.addTotalToOrder(this.ordersRepository.findAll(limit,offset,sort,order,showTimes,type,userId,status,
                null,this.convertLocalDate(dateGte),null)).stream().filter(item -> {
            return this.showTimesDetailService.findById(item.getShowTimesDetailId()).getTheaterId()
                    == this.userService.getTheaterByUser();
        }).collect(Collectors.toList());
    }


    @Cacheable(value = "OrdersService" ,key = "'findAllOrders_'+#limit +'-'+#offset +'-'" +
            "+#sort +'-'+#order +'-'+#showTimes+'-'+#order +'-'+#type+'-'+#userId +'-'+#status" +
            "+'-'+#creation +'-'+#dateGte+'-'+#isYear", unless = "#result == null")
    public List<Orders> findAllOrders(Integer limit, Integer offset, String sort, String order, Integer showTimes,
                                      String type, Integer userId,String status,
                                      Integer creation,String dateGte,Boolean isYear){
        return this.ordersRepository.findAll(limit,offset,sort,order,showTimes,type,userId,status,creation,dateGte,isYear);
    }



    @Override
    public List<OrderRoomDto> findAllOrderRoom(Integer limit, Integer offset, String sort, String order,
                                               Integer showTimes, String type, Integer userId,
                                               String status, Integer creation, String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        var isSenior =  this.userService.isSeniorManager(idUserContext);
        var isManager= this.userService.isManager(idUserContext);
        var listOrders = this.ordersRepository.findAll(limit,offset,sort,
                order,showTimes,type,userId,status, isManager || isSenior ? null :
                        idUserContext, this.convertLocalDate(dateGte),true);
        return this.addInfoOrders(this.addTotalToOrder(listOrders),isManager,isSenior);
    }

    private List<OrderRoomDto> addInfoOrders(List<Orders> orders,boolean isManager,boolean isSeniorManager){
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

    private List<OrderStatistics> addInfoOrderStatistics(List<OrderStatistics> orders,
                                                  boolean isManager,boolean isSeniorManager){
        var orderStatistics = new ArrayList<OrderStatistics>();
        for (var order: orders){
            var orderStatistic = OrderStatistics.builder()
                    .userId(order.getUserId()).createdDate(order.getCreatedDate())
                    .creation(order.getCreation()).id(order.getId()).tax(order.getTax()).profile(order.isProfile())
                    .status(order.getStatus()).total(order.getTotal())
                    .updatedBy(order.getUpdatedBy())
                    .showTimesDetailId(order.getShowTimesDetailId())
                    .totalSeats(order.getTotalSeats()).totalConcessions(order.getTotalConcessions())
                    .build();
            var showTimes = this.showTimesDetailService.findById(order.getShowTimesDetailId());
            orderStatistic.setRoomName(showTimes.getRoomName());
            orderStatistic.setTimeStart(showTimes.getTimeStart());
            var theater = this.theaterService.findById(showTimes.getTheaterId());
            orderStatistic.setTheaterName(theater.getName());
            var location  = this.locationService.findById(theater.getLocationId());
            var movie  = this.movieService.findById(showTimes.getMovieId());
            orderStatistic.setMovieName(movie.getName());
            orderStatistic.setLocationName(location.getName());
            orderStatistics.add(orderStatistic);
        }
        return isSeniorManager ? orderStatistics : isManager ? orderStatistics.stream().filter(
                order ->{
                    var item = this.showTimesDetailService.findById(order.getShowTimesDetailId());
                    return item.getTheaterId() == this.userService.getTheaterByUser();
                }
        ).collect(Collectors.toList()): orderStatistics;
    }



    @Override
    public List<Orders> findCountAllOrderRoom(Integer page, Integer size, String sort, String order, Integer showTimes, String type, Integer userId, String status, Integer creation, String dateGte) {
        return null;
    }


    @Override
    public List<Orders> findAllMyOrders(Integer page, Integer size, String sort, String order,
                                        Integer showTimes, String type, String status, Integer creation,
                                        String dateGte,
            Boolean isYear) {
        return this.addTotalToOrder(this.findAllMyOrder(size, page * size,
                sort,order,showTimes ,type,status, userService.getUserFromContext(),
                this.convertLocalDate(dateGte), isYear));
    }

    @Override
    @Cacheable(value = "OrdersService" ,key = "'findOrderStatistics_'+#creation +'-'+#dateGte", unless = "#result == null")
    public List<OrderStatistics> findOrderStatistics(Integer creation, String dateGte) {
        var isSenior =  this.userService.isSeniorManager(creation);
        var isManager= this.userService.isManager(creation);
        var listOrders = this.ordersRepository.findOrderStatistics( isManager || isSenior ? null :
                creation, this.convertLocalDate(dateGte));
        var addPriceOrders = this.addTotalToOrderStatistics(listOrders);
        return this.addInfoOrderStatistics(addPriceOrders,isManager,isSenior);
    }

    @Cacheable(value = "OrdersService" ,key = "'findAllMyOrder_'+#limit +'-'+#offset +'-'" +
            "+#sort +'-'+#order +'-'+#showTimes+'-'+#order +'-'+#type+'-'+#status" +
            "+'-'+#creation +'-'+#dateGte+'-'+#isYear", unless = "#result == null")
    public List<Orders> findAllMyOrder(int limit, int offset, String sort, String order,
                                       Integer showTimes, String type, String status, Integer creation,
                                       String dateGte,
                                       Boolean isYear){
        return this.ordersRepository.findMyOrders(limit, offset,
                sort,order,showTimes > 0 ? showTimes :null ,type,status,creation,
                this.convertLocalDate(dateGte), isYear ? true : null);
    }


    private String convertLocalDate(String date){
        if(date != null && !date.isEmpty()){
            return Utilities.convertIsoToDate(date);
        }
        return null;
    }

    @Override
    @Cacheable(value = "OrdersService" ,key = "'findAllMyOrder_'+#showTimes+'-'+#type+'-'+#status" +
            "+'-'+#creation +'-'+#dateGte+'-'+#isYear", unless = "#result == null")
    public int findCountAllMyOrder(Integer showTimes, String type, String status, Integer creation,String dateGte,Boolean isYear) {
        return this.ordersRepository.findCountAllMyOrder(showTimes > 0 ? showTimes :null,
                type,status,creation > 0 ? creation : null,this.convertLocalDate(dateGte),isYear ? true : null);
    }

    @Override
    public int findAllOrderManagerCount(Integer showTimes, String type, Integer userId, String status, Integer creation, String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        var isSenior  = this.userService.isSeniorManager(idUserContext);
        var manager =  this.userService.isManager(idUserContext);

        return isSenior ? this.findAllOrders(null, null,"createdDate", "DESC",showTimes,type,userId,status,
                isSenior || manager ? null : idUserContext,
                this.convertLocalDate(dateGte),null).size() :
                this.findAllOrderManager(null, null,"createdDate", "DESC",showTimes,type,userId,status, null,
                        this.convertLocalDate(dateGte)).size();
    }

    @Override
    public int findAllCount(Integer showTimes, String type, Integer userId,
                            String status,Integer creation,String dateGte) {
        var idUserContext = this.userService.getUserFromContext();
        var isSenior  = this.userService.isSeniorManager(idUserContext);

        return isSenior ? this.findAll(null, null,"createdDate", "DESC",showTimes,type,userId,status,
                null , this.convertLocalDate(dateGte)).size() :
                (int) this.findAll(null, null, "createdDate", "DESC", showTimes, type, userId, status,
                        null, this.convertLocalDate(dateGte)).stream().filter(orders -> {
                    return this.showTimesDetailService.findById(orders.getShowTimesDetailId()).getTheaterId()
                            == this.userService.getTheaterByUser();
                }).count();

    }

    private double totalConcession(List<ConcessionMyOrder> concessionMyOrders){
        double totalAmount = 0.d;
        for (var concession: concessionMyOrders){
            totalAmount += concession.getPrice() * concession.getQuantity();
        }
        return totalAmount;
    }

    private double totalSeats(List<OrderSeats> seats, ShowTimesDetail showTime){
        double totalAmount = 0.d;
        for (var seat : seats){
            totalAmount += showTime.getPrice();
        }
        return totalAmount;
    }

    private OrderStatistics getTotalOrder(OrderStatistics orders){
        var concessions = this.concessionRepository.findAllConcessionInOrder(orders.getId());
        var seats = this.ordersRepository.findSeatInOrders(orders.getId());
        var totalConcession = this.totalConcession(concessions);
        var showTime = this.showTimesDetailService.findById(orders.getShowTimesDetailId());
        var totalSeat = this.totalSeats(seats,showTime);
        double totalAmount = totalConcession + totalSeat;
        var taxAmount = (totalAmount / 100) * orders.getTax();
        orders.setTotal(totalAmount + taxAmount);
        orders.setTotalSeats(totalSeat);
        orders.setTotalConcessions(totalConcession);
        return orders;
    }


    private double getTotalOrder(Orders orders){
        var concessions = this.concessionRepository.findAllConcessionInOrder(orders.getId());
        var seats = this.ordersRepository.findSeatInOrders(orders.getId());
        var totalConcession = this.totalConcession(concessions);
        var showTime = this.showTimesDetailService.findById(orders.getShowTimesDetailId());
        var totalSeat = this.totalSeats(seats,showTime);
        double totalAmount = totalConcession + totalSeat;
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
    @Cacheable(value = "OrdersService" ,key = "'findMyOrderById_'+#id", unless = "#result == null")
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
    public Orders findOrdersById(int id) {
        return this.ordersRepository.findOrderById(id);
    }

    @Override
    @CacheEvict(cacheNames = "OrdersService",allEntries = true)
    public void delete(int id) {
        var orders = this.findById(id);
        this.ordersRepository.delete(orders.getId());
    }

    @Override
    @CacheEvict(cacheNames = "OrdersService",allEntries = true)
    public int insert(Orders orders) throws SQLException {
        String sql = "Insert into orders(user_id,showtimes_detail_id,created_date,note,creation,profile,status,expire_payment,total) values (?,?,?,?,?,?,?,?,?)";
        return this.ordersCustomRepository.insert(orders,sql);
    }

    @Override
    @CacheEvict(cacheNames = "OrdersService",allEntries = true)
    public int update(Orders orders) {
        return this.ordersRepository.update(orders);
    }


    private boolean isSeatsAvailable(Integer seat,List<Seat> idSeats){
        for(var item : idSeats){
            if(item.getId() == seat) return true;
        }
        return false;
    }

    public void checkSeatIsAvailable(OrderDto orderDto){
        var idSeats = this.seatService.findAllSeatInShowTimeUnavailable(orderDto.getShowTimesDetailId());
        for (var seat : orderDto.getSeats()){
            if(isSeatsAvailable(seat,idSeats)){
                throw new NotFoundException("Seat !" + seat + "reserved!");
            }
        }
    }

    @Override
    @Caching(evict =
            { @CacheEvict(value = "OrdersService",allEntries = true),
                    @CacheEvict(value = "SeatService",allEntries = true)
            })
    public int orderNonPayment(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException {
        checkSeatIsAvailable(orderDto);
        return reservedSeatAndConcession(orderDto,this.userService.getUserFromContext(),0);
    }

    private int reservedSeatAndConcession(OrderDto orderDto,int creation,int userId) throws ExecutionException, InterruptedException, SQLException {
        var taxAmount = (orderDto.getTotalAmount() / 100) * 10;
        Orders orders = Orders.builder()
                .creation(creation)
                .showTimesDetailId(orderDto.getShowTimesDetailId())
                .total(orderDto.getTotalAmount() + taxAmount)
                .profile(orderDto.getTypeUser())
                .userId(orderDto.getUserId())
                .status(OrderStatus.NON_PAYMENT.getStatus())
                .expirePayment(Utilities.getTimeExpirePayment5m())
                .userId(userId)
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
            this.sendDataToClient();
            this.seatService.sendDataToClient(orderDto.getShowTimesDetailId());
        }else{
            return 0;
        }
        return idOrderCreated;
    }

    @Override
    @Caching(evict =
            { @CacheEvict(value = "OrdersService",allEntries = true),
                    @CacheEvict(value = "SeatService",allEntries = true)
            })
    public int orderNonPaymentAnonymous(OrderDto orderDto) throws SQLException, ExecutionException, InterruptedException {
        checkSeatIsAvailable(orderDto);
        return reservedSeatAndConcession(orderDto,orderDto.getUserId(),orderDto.getUserId());
    }


    @Override
    @CacheEvict(value = "OrdersService",allEntries = true)
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

    @Override
    public void sendDataToClient() throws ExecutionException, InterruptedException {
        var userId = this.userService.getUserFromContext();
        var listOrderNew = this.findOrderStatistics(userId,Utilities.subDate(30));
        var listOrders = this.findOrderStatistics(userId,null);
        List<Object> objectList = new ArrayList<>();
        objectList.add(listOrderNew);
        objectList.add(listOrders);
        kafkaTemplate.send("test-websocket","order-chart",
                new com.apps.config.kafka.Message("order",objectList )).get();

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
