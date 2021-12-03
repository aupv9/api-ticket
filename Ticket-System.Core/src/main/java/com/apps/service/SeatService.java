package com.apps.service;

import com.apps.domain.entity.Seat;
import com.apps.request.SeatDto;


import java.sql.SQLException;
import java.util.List;

public interface SeatService {
    List<Seat> findAll(Integer page, Integer size,String sort ,String order, String search, Integer room);
    int findCountAll(String search, Integer room);


    int insert(SeatDto seat) throws SQLException;
    int update(Seat seat);

    Seat findById(Integer id);

    List<List<Seat>> findSeatInRoomByShowTimesDetail( Integer showTimesDetailId,Integer roomId);
    Boolean validateSeat( Integer room, String tier, Integer numbers);
    void delete(Integer id);
    List<SeatDto> findByRoom(Integer page, Integer size,String sort ,String order,Integer room,Integer showTimes);
    List<List<Seat>> findByRoomShow( Integer showTimesDetailId,Integer roomId);

}
