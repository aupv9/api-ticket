package com.apps.service;

import com.apps.domain.entity.Seat;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface SeatService {
    List<Seat> findAll(Integer page, Integer size);
    int insert(Seat seat) throws SQLException;
    Seat findById(Integer id);

    List<List<Seat>> findSeatInRoomByShowTimesDetail( Integer showTimesDetailId,Integer roomId);

}
