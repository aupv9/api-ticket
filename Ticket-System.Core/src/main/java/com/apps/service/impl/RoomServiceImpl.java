package com.apps.service.impl;

import com.apps.domain.entity.Room;
import com.apps.domain.repository.RoomCustomRepository;
import com.apps.mybatis.mysql.RoomRepository;
import com.apps.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    @Qualifier(value = "commonRepository")
    private RoomCustomRepository roomCustomRepository;

    @Override
    @Cacheable(value = "RoomService" ,key = "'RoomList_'+#page +'-'+#size+'-'+#sort +'-'+#order +'-'+#search+'-'+#theater", unless = "#result == null")
    public List<Room> findAll(Integer page, Integer size,
                              String sort, String order,
                              String search,
                              Integer theater) {
        return this.roomRepository.findAll(size,page*size,sort,order,search,theater);
    }

    @Override
    public int insert(Room room) throws SQLException {
        String sql = "INSERT INTO booksystem.room (code, name, theater_id) VALUES (?,?,?)";
        return this.roomCustomRepository.insert(room,sql);
    }
}
