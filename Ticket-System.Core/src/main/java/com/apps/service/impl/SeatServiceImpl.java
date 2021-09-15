package com.apps.service.impl;

import com.apps.domain.entity.Seat;
import com.apps.domain.repository.SeatCustomRepository;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatCustomRepository seatCustomRepository;

    @Override
    public List<Seat> findAll(Integer page, Integer size) {
        return seatRepository.findAll(size, page * size);
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
}
