package com.apps.service;


import com.apps.domain.entity.SeatDataRoom;
import com.apps.domain.entity.SeatRoom;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface SeatRoomService {
    List<SeatRoom> findAll(Integer page, Integer size);

    List<List<SeatDataRoom>> findSeatInRoomByShowTimes(int showTimesId);

    int insert(SeatRoom SeatRoom) throws SQLException;

    SeatRoom findById(int id);

    List<SeatRoom> findByAll(int roomId,  int showTimesDetailId,
                             int seatId, int tierId);

    int reservedSeat(int id) throws SQLException;

    int expireReservedSeat(int id) throws SQLException;
}
