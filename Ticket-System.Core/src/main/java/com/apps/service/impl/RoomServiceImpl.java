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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
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
    @Cacheable(value = "RoomService" ,key = "'RoomList_'+#page +'-'+#size+" +
            "'-'+#sort +'-'+#order +'-'+#search+'-'+#theater+'-'+#isSeniorManager", unless = "#result == null")
    public List<Room> findAll(Integer page, Integer size, String sort, String order, String search,
                              Integer theater,Boolean isSeniorManager) {
        return this.roomRepository.findAll(size,page * size,sort,order,search,isSeniorManager ? null : theater);
    }



    @Override
    @CacheEvict(value = "RoomService",allEntries = true)
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
        return result;
    }

    @Override
    @Cacheable(value = "RoomService" ,key = "'findByRoom_'+#id", unless = "#result == null")
    public RoomDto findById(Integer id) {
        Room room = this.roomRepository.findById(id);
        var roomDto = RoomDto.builder().id(room.getId())
                .code(room.getCode()).name(room.getName())
                .theaterId(room.getTheaterId())
                .active(room.isActive())
                .build();
        var roomService = this.serviceRepository.findRoomServiceByRoom(room.getId());
        var listService = new ArrayList<Integer>();
        for (var item : roomService){
            listService.add(item.getServiceId());
        }
        roomDto.setServices(listService);
        return roomDto;
    }

    @Override
    @Cacheable(value = "RoomService" ,key = "'findByCode_'+#code", unless = "#result == null")
    public Room findByCode(String code) {
        return this.roomRepository.findByCode(code);
    }

    @Override
    @CacheEvict(cacheNames = "RoomService",allEntries = true)
    public int update(RoomDto roomDto) {
        Room room1 = this.roomRepository.findById(roomDto.getId());
        room1.setName(roomDto.getName());
        room1.setCode(roomDto.getCode());
        room1.setActive(roomDto.isActive());
        room1.setTheaterId(roomDto.getTheaterId());
        this.serviceRepository.deleteByRoom(room1.getId());
        for (var service : roomDto.getServices()){
            this.serviceRepository.insertRoomService(room1.getId(),service);
        }
        return this.roomRepository.update(room1);
    }

    @Override
    @CacheEvict(value = "RoomService",allEntries = true)
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

    @Override
    @Cacheable(value = "RoomService" ,
            key = "'countSeatById_'+#room", unless = "#result == null")
    public int countSeatById(Integer room) {
        return this.roomRepository.countSeatById(room);
    }

    @Override
    @Cacheable(value = "RoomService" ,
            key = "'findByTheater_'+#theater", unless = "#result == null")
    public List<Room> findByTheater(Integer theater) {
        return this.roomRepository.findByTheater(theater);
    }


}
