package com.apps.service;

import com.apps.domain.entity.Room;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


public interface RoomService {
    List<Room> findAll(Integer page, Integer size);
    int insert(Room room) throws SQLException;
}
