package com.apps.service.impl;

import com.apps.domain.entity.SeatRoom;
import com.apps.domain.repository.SeatRoomCustomRepository;
import com.apps.mybatis.mysql.SeatRoomRepository;
import com.apps.service.SeatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SeatRoomServiceImpl implements SeatRoomService {

    @Autowired
    private SeatRoomRepository roomRepository;

    @Autowired
    private SeatRoomCustomRepository roomCustomRepository;

    @Override
    public List<SeatRoom> findAll(Integer page, Integer size) {
        return this.roomRepository.findAll(size, page * size);
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
    public int reservedSeat(int id, boolean status) throws SQLException {
        int result = this.roomRepository.reservedSeat(id, status);
        if(result > 0){
            this.roomCustomRepository.clockSeatById(id);
        }
        return result;
    }


}
