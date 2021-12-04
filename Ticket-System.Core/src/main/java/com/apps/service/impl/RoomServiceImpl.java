package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Room;
import com.apps.domain.repository.RoomCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.RoomRepository;
import com.apps.mybatis.mysql.ServiceRepository;
import com.apps.request.RoomDto;
import com.apps.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final RoomCustomRepository roomCustomRepository;

    private final ApplicationCacheManager cacheManager;

    private final UserServiceImpl userService;

    private final ServiceRepository serviceRepository;

    @Override
    @Cacheable(value = "RoomService" ,key = "'RoomList_'+#page +'-'+#size+'-'+#sort +'-'+#order +'-'+#search+'-'+#theater+'-'+#isSeniorManager", unless = "#result == null")
    public List<Room> findAll(Integer page, Integer size, String sort, String order, String search,
                              Integer theater,Boolean isSeniorManager) {
        return this.roomRepository.findAll(size,page * size,sort,order,search,isSeniorManager ? null : theater);
    }

    @Override
    public int insert(RoomDto room) throws SQLException {
        String sql = "INSERT INTO room (code, name, theater_id) VALUES (?,?,?)";
        var room1 = Room.builder()
                .name(room.getName()).code(room.getCode())
                .build();
        if(room.getTheaterId() != 0){
            room1.setTheaterId(room.getTheaterId());
        }else{
            room.setTheaterId(this.userService.getTheaterByUser());
        }
        int result = this.roomCustomRepository.insert(room1,sql);
        for (var service : room.getServices()){
            this.serviceRepository.insertRoomService(result,service);
        }
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
    public int update(RoomDto roomDto) {
        Room room1 = this.roomRepository.findById(roomDto.getId());
        room1.setName(roomDto.getName());
        room1.setCode(roomDto.getName());
        room1.setActive(roomDto.isActive());
        room1.setTheaterId(roomDto.getTheaterId());
        for (var service : roomDto.getServices()){
            this.serviceRepository.insertRoomService(room1.getId(),service);
        }
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
    @Cacheable(value = "RoomService" ,key = "'findCountAllRoom_'+#search+'-'+#theater+'-'+#isSeniorManager", unless = "#result == null")
    public int findCountAll(String search, Integer theater,Boolean isSeniorManager) {
        return this.roomRepository.findCountAll(search, isSeniorManager ? null : theater);
    }


}
