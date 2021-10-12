package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Seat;
import com.apps.domain.repository.SeatCustomRepository;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
import javafx.application.Application;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatCustomRepository seatCustomRepository;

    @Autowired
    private ApplicationCacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'SeatList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#search+'-'+#room",unless = "#result == null")
    public List<Seat> findAll(Integer page, Integer size, String sort , String order, String search, Integer room) {
        return seatRepository.findAll(size, page * size, sort, order,  search,  room);
    }

    @Override
    public int findCountAll(String search, Integer room) {
        return seatRepository.findCountAll(search,room);
    }

    @Override
    public int insert(Seat seat) throws SQLException {
        String sql = "insert into seat(price, seat_type, tier, numbers,room_id) VALUES (?,?,?,?,?)";
        int result = this.seatCustomRepository.insert(seat,sql);
        if(result < 0) return 0;
        cacheManager.clearCache("SeatService");
        return result;
    }

    @Override
    public int update(Seat seat) {
        Seat seat1 = findById(seat.getId());
        seat1.setSeatType(seat.getSeatType());
        seat1.setRoomId(seat.getRoomId());
        seat1.setTier(seat.getTier());
        seat1.setNumbers(seat.getNumbers());
        seat1.setPrice(seat.getPrice());
        int result = this.seatRepository.update(seat1);
        if(result < 0) return 0;
        cacheManager.clearCache("SeatService");
        return result;
    }

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'findById_'+#id",unless = "#result == null")
    public Seat findById(Integer id) {
        return this.seatRepository.findById(id);
    }

    @Override
    public List<List<Seat>> findSeatInRoomByShowTimesDetail(Integer showTimesDetailId, Integer roomId) {
        Map<String,List<Seat>> map = new HashMap<>();
        var list = this.seatRepository.findSeatInRoomByShowTimesDetail(showTimesDetailId,roomId);
        for (var item:list){
            var key = item.getTier();
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
                map.get(key).add(item);
            }else{
                map.get(key).add(item);
            }
        }
        var listResult = new ArrayList<List<Seat>>();
        for (var item: map.entrySet()){
            listResult.add(item.getValue());
        }
        return listResult;
    }

    @Override
    public Boolean validateSeat(Integer room, String tier, Integer numbers) {
        return this.seatRepository.validateSeat(room,tier,numbers) == null;
    }

    @Override
//    @CacheEvict(value = "SeatService",key = "'findById_'+#id")
    public void delete(Integer id) {
        Seat seat = this.seatRepository.findById(id);
        this.seatRepository.delete(seat.getId());
        cacheManager.clearCache("SeatService");
    }

    @Override
    public List<Seat> findByRoom(Integer room) {
        return this.seatRepository.findByRoom(room);
    }
}
