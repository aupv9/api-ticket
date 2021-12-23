package com.apps.service;

import com.apps.domain.entity.Room;
import com.apps.domain.entity.Seat;
import com.apps.request.RoomDto;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


public interface RoomService {
    List<Room> findAll(Integer page, Integer size,
                       String sort, String order,
                       String search,
                       Integer theater,Boolean isSeniorManager);
    int insert(RoomDto room) throws SQLException;
    RoomDto findById(Integer id);
    Room findByCode(String code);

    int update(RoomDto room);
    void delete(Integer id);
    int findCountAll(String search, Integer theater,Boolean isSeniorManager);

    int countSeatById(Integer room);

    List<Room> findByTheater(Integer theater);


}
