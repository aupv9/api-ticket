package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Orders;
import com.apps.domain.repository.OrdersCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.OrdersRepository;
import com.apps.service.OrdersService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersCustomRepository ordersCustomRepository;
    private final ApplicationCacheManager cacheManager;
    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersCustomRepository ordersCustomRepository, ApplicationCacheManager cacheManager) {
        this.ordersRepository = ordersRepository;
        this.ordersCustomRepository = ordersCustomRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public List<Orders> findAll(int page, int size, String sort, String order, Integer showTimes, String type, Integer userId,String status) {
        return this.ordersRepository.findAll(size, page * size,sort,order,showTimes,type,userId,status);
    }

    @Override
    public int findAllCount(Integer showTimes, String type, Integer userId,String status) {
        return this.ordersRepository.findCountAll(showTimes,type,userId,status);
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
        String sql = "Insert into (user_id,showtimes_detail_id,tax,create_date,note,creation,type_user,status) values (?,?,?,?,?,?,?,?)";
        int result = this.ordersCustomRepository.insert(orders,sql);
        if(result > 0 && orders.getConcessionId() > 0){
            this.ordersRepository.insertOrderConcession(orders.getConcessionId(),result);
            this.ordersRepository.insertOrderSeat(orders.getSeatId(),result);
        }
        return result;
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
        orders1.setCreateDate(orders.getCreateDate());
        int result = this.ordersRepository.update(orders1);
        return result;
    }
}
