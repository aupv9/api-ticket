package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.SeatStatus;
import com.apps.domain.entity.Reserved;
import com.apps.domain.entity.Seat;
import com.apps.domain.repository.SeatCustomRepository;
import com.apps.mapper.ReservedDto;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.request.SeatDto;
import com.apps.response.ResponseRA;
import com.apps.service.SeatService;
import com.apps.service.ShowTimesDetailService;
import com.apps.service.TicketService;
import javafx.application.Application;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final SeatCustomRepository seatCustomRepository;

    private final ApplicationCacheManager cacheManager;

    private final ShowTimesDetailService showTimesDetailService;

    private final TicketService ticketService;

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'SeatList_findAll_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#search+'-'+#room",unless = "#result == null")
    public List<Seat> findAll(Integer page, Integer size, String sort , String order, String search, Integer room) {
        return seatRepository.findAll(size, page * size, sort, order,  search,  room);
    }

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'SeatList_findCountAll_'+#search +'-'+#room",unless = "#result == null")
    public int findCountAll(String search, Integer room) {
        return seatRepository.findCountAll(search,room);
    }

    @Override
    public int insert(SeatDto seatDto) throws SQLException {
        String sql = "insert into seat(seat_type, tier, numbers,room_id) VALUES (?,?,?,?)";
        var seat = Seat.builder()
                .tier(seatDto.getTier()).numbers(seatDto.getNumbers())
                .seatType(seatDto.getSeatType()).roomId(seatDto.getRoomId())
                .build();
        int result = this.seatCustomRepository.insert(seat,sql);
        if(result < 0) return 0;
        cacheManager.clearCache("SeatService");
        return result;
    }

    @Override
    public int update(Seat seat) {
        Seat seat1 = findById(seat.getId());
        seat1.setSeatType(seat.getSeatType());
        seat1.setRoomId(seat.getRoomId());
        seat1.setTier(seat.getTier());
        seat1.setNumbers(seat.getNumbers());
        int result = this.seatRepository.update(seat1);
        if(result < 0) return 0;
        cacheManager.clearCache("SeatService");
        return result;
    }

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'findById_'+#id",unless = "#result == null")
    public Seat findById(Integer id) {
        return this.seatRepository.findById(id);
    }

    @Override
    public List<List<Seat>> findSeatInRoomByShowTimesDetail(Integer showTimesDetailId, Integer roomId) {
        var list = this.seatRepository.findSeatInRoomByShowTimesDetail(showTimesDetailId,roomId);
        var reservedList = this.ticketService.findByRoomShowTime(roomId,showTimesDetailId);
        return this.seatToSeat(list);
    }

    private List<List<Seat>> seatToSeat(List<Seat> list) {
        Map<String,List<Seat>> map = new HashMap<>();
        for (var item:list){
            var key = item.getTier();
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
                map.get(key).add(item);
            }else{
                map.get(key).add(item);
            }
        }
        var listResult = new ArrayList<List<Seat>>();
        for (var item: map.entrySet()){
            listResult.add(item.getValue());
        }
        return listResult;
    }

    @Override
    public Boolean validateSeat(Integer room, String tier, Integer numbers) {
        return this.seatRepository.validateSeat(room,tier,numbers) == null;
    }

    @Override
//    @CacheEvict(value = "SeatService",key = "'findById_'+#id")
    public void delete(Integer id) {
        Seat seat = this.seatRepository.findById(id);
        this.seatRepository.delete(seat.getId());
        cacheManager.clearCache("SeatService");
    }

    @Override
//    @Cacheable(cacheNames = "SeatService",key = "'findByRoom_'+#page +'-'+#size+'-'+#sort+'-'+#order+'-'+#room+'-'+#showTimes",unless = "#result == null")
    public List<SeatDto> findByRoom(Integer page, Integer size,String sort ,String order,Integer room,Integer showTimes) {
        var arrSeat = this.seatRepository.findAll(size,(page -1 ) * size,sort,order,null,room);
        var arrSeatAvailable = this.seatRepository.findSeatInRoomByShowTimesDetail(showTimes,room);
        List<Integer> arrIdSeatAvailable = new ArrayList<>();
        for (Seat seat : arrSeatAvailable){
            arrIdSeatAvailable.add(seat.getId());
        }
        for (Seat seat : arrSeat) {
            if (arrIdSeatAvailable.contains(seat.getId())) {
                seat.setIsSelected(false);
                seat.setStatus(SeatStatus.Available.name());
            } else {
                seat.setIsSelected(true);
                seat.setStatus(SeatStatus.Unavailable.name());
            }
        }
        return this.convertSeatToSeatHavePrice(arrSeat,showTimes);
    }

    private List<SeatDto> convertSeatToSeatHavePrice(List<Seat> seats,int showId){
        var seatList = new ArrayList<SeatDto>();
        var showTime = this.showTimesDetailService.findById(showId);
        seats.forEach(seat -> {
            var seatDto = SeatDto.builder()
                    .id(seat.getId()).seatType(seat.getSeatType())
                    .isSelected(seat.getIsSelected()).status(seat.getStatus())
                    .tier(seat.getTier()).numbers(seat.getNumbers()).roomId(seat.getRoomId())
                    .price(showTime.getPrice())
                    .build();
            seatList.add(seatDto);
        });
        return seatList;
    }


    @Override
    public List<List<Seat>> findByRoomShow(Integer showTimesDetailId, Integer roomId) {
        return null;
    }

//    @Override
//    public List<Seat> findByShowTimes(int showTimes) {
//        var showTimesDetail = this.showTimesDetailService.findById(showTimes);
//        if(showTimesDetail != null){
//           return this.findByRoom(null,showTimesDetail.getRoomId(),showTimes);
//        }
//        return new ArrayList<>();
//    }

//    @Override
//    public List<List<Seat>> findByRoomShow(Integer showTimesDetailId, Integer roomId) {
//        var arrSeat = this.findByRoom(showTimesDetailId,roomId);
//        var seatWithRow = this.seatToSeat(arrSeat);
//        return seatWithRow;
//    }
}
