package com.apps.service.impl;

import com.apps.domain.entity.Seat;
import com.apps.domain.repository.SeatCustomRepository;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql = "insert into booksystem.seat(price, id, seat_type_id, tier_id) VALUES (?,?,?,?)";
        return this.seatCustomRepository.insert(seat,sql);
    }

    @Override
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
}
