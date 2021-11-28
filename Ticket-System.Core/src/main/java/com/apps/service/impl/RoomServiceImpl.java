package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Room;
import com.apps.domain.entity.Seat;
import com.apps.domain.repository.RoomCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.RoomRepository;
import com.apps.service.RoomService;
import lombok.var;
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

    @Autowired
    private ApplicationCacheManager cacheManager;

    @Autowired
    private UserServiceImpl userService;

    @Override
//    @Cacheable(value = "RoomService" ,key = "'RoomList_'+#page +'-'+#size+'-'+#sort +'-'+#order +'-'+#search+'-'+#theater", unless = "#result == null")
    public List<Room> findAll(Integer page, Integer size, String sort, String order, String search,
                              Integer theater) {
        return this.roomRepository.findAll(size,page * size,sort,order,search,theater);
    }

    @Override
    public int insert(Room room) throws SQLException {
        String sql = "INSERT INTO booksystem.room (code, name, theater_id,type) VALUES (?,?,?,?)";
        room.setTheaterId(this.userService.getTheaterByUser());
        int result = this.roomCustomRepository.insert(room,sql);
        cacheManager.clearCache("RoomService");
        return result;
    }

    @Override
    @Cacheable(value = "RoomService" ,key = "'findByRoom_'+#id", unless = "#result == null")
    public Room findById(Integer id) {
        Room room1 = this.roomRepository.findById(id);
        if(room1 == null){
            throw new NotFoundException("Not Found Object have Id:"+ id);
        }
        return this.roomRepository.findById(id);
    }

    @Override
    public int update(Room room) {
        Room room1 = this.roomRepository.findById(room.getId());
        room1.setName(room.getName());
        room1.setCode(room.getName());
        room1.setType(room.getType());
        room1.setTheaterId(room.getTheaterId());
        int result = this.roomRepository.update(room1);
        cacheManager.clearCache("RoomService");
        return result;
    }

    @Override
    public void delete(Integer id) {
        Room room = this.roomRepository.findById(id);
        this.roomRepository.delete(id);
        cacheManager.clearCache("RoomService");
    }

    @Override
    @Cacheable(value = "RoomService" ,key = "'findCountAllRoom_'+#search+'-'+#theater", unless = "#result == null")
    public int findCountAll(String search, Integer theater) {
        var theaterId = this.userService.getTheaterByUser();
        return this.roomRepository.findCountAll(search, theater);
    }


}
