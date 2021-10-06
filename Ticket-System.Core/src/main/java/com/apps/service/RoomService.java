package com.apps.service;

import com.apps.domain.entity.Room;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


public interface RoomService {
    List<Room> findAll(Integer page, Integer size,
                       String sort, String order,
                       String search,
                       Integer theater);
    int insert(Room room) throws SQLException;
    Room findById(Integer id);
    int update(Room room);
    void delete(Integer id);
    int findCountAll(String search, Integer theater);

}
