package com.apps.service.impl;

import com.apps.contants.StatusSeat;
import com.apps.domain.entity.SeatDataRoom;
import com.apps.domain.entity.SeatRoom;
import com.apps.domain.repository.SeatRoomCustomRepository;
import com.apps.mybatis.mysql.SeatRoomRepository;
import com.apps.service.SeatRoomService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class SeatRoomServiceImpl implements SeatRoomService {

    @Autowired
    private SeatRoomRepository roomRepository;

    @Autowired
    private SeatRoomCustomRepository roomCustomRepository;

    @Override
    @Cacheable(cacheNames = "SeatRoomService",key = "'SeatRoomList_'+#page +'-'+#size")
    public List<SeatRoom> findAll(Integer page, Integer size) {
        return this.roomRepository.findAll(size, page * size);
    }

    @Override
    public List<List<SeatDataRoom>> findSeatInRoomByShowTimes(int showTimesId) {
        Map<String,List<SeatDataRoom>> map = new HashMap<>();
        var list = this.roomRepository.findSeatInRoomByShowTimes(showTimesId);
        for (var item:list){
            var key = item.getTierName();
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<SeatDataRoom>());
                map.get(key).add(item);
            }
            map.get(key).add(item);
        }
        var listResult = new ArrayList<List<SeatDataRoom>>();
        for (var item: map.entrySet()){
            listResult.add(item.getValue());
        }
        log.info("List Result: "+ listResult);
        return listResult;
    }

    @Override
    public int insert(SeatRoom SeatRoom) throws SQLException {
        return 0;
    }

    @Override
    public SeatRoom findById(int id) {
        return this.roomRepository.findById(id);
    }

    @Override
    public List<SeatRoom> findByAll(int roomId, int showTimesDetailId, int seatId, int tierId) {
        return this.roomRepository.findByAll(roomId,showTimesDetailId,seatId,tierId);
    }

    @Override
    public int reservedSeat(int id) throws SQLException {
        int result = this.roomCustomRepository.clockSeatById(id, StatusSeat.PENDING.getStatus());
        log.info("Result : "+ result);
        return result;
    }

    @Override
    public int expireReservedSeat(int id) throws SQLException {
        int result = this.roomCustomRepository.clockSeatById(id, StatusSeat.AVAILABLE.getStatus());
        log.info("Result : "+ result);

        return 0;
    }


}
