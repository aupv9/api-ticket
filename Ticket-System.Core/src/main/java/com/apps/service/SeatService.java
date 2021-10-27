package com.apps.service;

import com.apps.domain.entity.Seat;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

public interface SeatService {
    List<Seat> findAll(Integer page, Integer size,String sort ,String order, String search, Integer room);
    int findCountAll(String search, Integer room);


    int insert(Seat seat) throws SQLException;
    int update(Seat seat);

    Seat findById(Integer id);

    List<List<Seat>> findSeatInRoomByShowTimesDetail( Integer showTimesDetailId,Integer roomId);
    Boolean validateSeat( Integer room, String tier, Integer numbers);
    void delete(Integer id);
    List<Seat> findByRoom(Integer room,Integer showTimes);
    List<Seat> findByShowTimes(int showTimes);

}
