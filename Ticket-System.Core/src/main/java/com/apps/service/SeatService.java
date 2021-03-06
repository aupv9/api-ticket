package com.apps.service;

import com.apps.domain.entity.Seat;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.request.SeatDto;


import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SeatService {
    List<Seat> findAll(Integer page, Integer size,String sort ,String order, String search, Integer room
            ,Integer idUserContext);
    int findCountAll(String search, Integer room,
                     Integer idUserContext);


    int insert(SeatDto seat) throws SQLException;
    int insert2(SeatDto seat) throws SQLException;

    int update(Seat seat);

    Seat findById(Integer id);

    List<List<Seat>> findSeatInRoomByShowTimesDetail( Integer showTimesDetailId,Integer roomId);
    Boolean validateSeat( Integer room, String tier, Integer numbers);
    void delete(Integer id);
    List<SeatDto> findByRoom(Integer limit, Integer offset,String sort ,String order,Integer room,Integer showTimes);
    List<SeatDto> findByTheater(String date,String time,Integer theater,Integer movie);


    int countSeatAvailable(Integer show,Integer room);
    List<List<Seat>> findByRoomShow( Integer showTimesDetailId,Integer roomId);
    List<Seat> findAllSeatInShowTimeUnavailable(Integer showTimesId);

    ShowTimesDetail findShowTimesById(Integer id);

    void sendDataToClient(Integer showTimesId) throws ExecutionException, InterruptedException;

    double percentCoverRoom(Integer room);
}
