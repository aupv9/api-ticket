package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.config.kafka.Message;
import com.apps.contants.SeatStatus;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Reserved;
import com.apps.domain.entity.Room;
import com.apps.domain.entity.Seat;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.repository.SeatCustomRepository;
import com.apps.mapper.ReservedDto;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.request.SeatDto;
import com.apps.response.ResponseRA;
import com.apps.service.*;
import javafx.application.Application;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final SeatCustomRepository seatCustomRepository;

    private final ApplicationCacheManager cacheManager;

    private final ShowTimesDetailService showTimesDetailService;

    private final TicketService ticketService;

    private final RoomService roomService;

    private final UserService userService;


    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Override
    @Cacheable(cacheNames = "SeatService",key = "'SeatList_findAll_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#search+'-'+#room",unless = "#result == null")
    public List<Seat> findAll(Integer page, Integer size, String sort , String order, String search, Integer room
            ,Integer idUserContext) {
        var isSenior  = this.userService.isSeniorManager(idUserContext);
        return isSenior ? seatRepository.findAll(size, page * size, sort, order,  search,  room, null) :
               seatRepository.findAll(size, page * size, sort, order,  search,  room, this.userService.getTheaterByUser()) ;
    }

    public List<Seat> listSeatByTheater(List<Seat> seats){
        return seats.stream().filter(seat -> {
            var room = this.roomService.findById(seat.getRoomId());
            return room.getTheaterId() == this.userService.getTheaterByUser();
        }).collect(Collectors.toList());
    }


    @Override
    @Cacheable(cacheNames = "SeatService",key = "'SeatList_findCountAll_'+#search+'-'+#room+'-'+#idUserContext",unless = "#result == null")
    public int findCountAll(String search, Integer room,Integer idUserContext) {
        var isSenior  = this.userService.isSeniorManager(idUserContext);
        return isSenior ? this.seatRepository.findCountAll(search,room,null) :
                this.seatRepository.findCountAll(search,room,this.userService.getTheaterByUser()) ;
    }

    @Override
    @CacheEvict(cacheNames = "SeatService",allEntries = true)
    public int insert(SeatDto seatDto) throws SQLException {
        String sql = "insert into seat(seat_type, tier, numbers,room_id) VALUES (?,?,?,?)";
        var seat = Seat.builder()
                .tier(seatDto.getTier()).numbers(seatDto.getNumbers())
                .seatType(seatDto.getSeatType()).roomId(seatDto.getRoomId())
                .build();
        return this.seatCustomRepository.insert(seat,sql);
    }

    @Override
    @CacheEvict(cacheNames = "SeatService",allEntries = true)
    public int insert2(SeatDto seat) throws SQLException {
        String sql = "insert into seat(seat_type, tier, numbers,room_id) VALUES (?,?,?,?)";
        var seat1 = Seat.builder()
                .seatType(seat.getType()).tier(seat.getTier())
                .numbers(seat.getNumbers())
                .build();
        var room = this.roomService.findByCode(seat.getRoom());
        if(room != null){
            seat1.setRoomId(room.getId());
        }else{
            var room1 = this.roomService.findByCode(seat.getRoom());
            seat1.setRoomId(room1.getId());
        }
        return this.seatCustomRepository.insert(seat1,sql);
    }

    @Override
    @CacheEvict(cacheNames = "SeatService",allEntries = true)
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
    @CacheEvict(value = "SeatService",key = "'deleteSeat_'+#id")
    public void delete(Integer id) {
        Seat seat = this.seatRepository.findById(id);
        this.seatRepository.delete(seat.getId());
        cacheManager.clearCache("SeatService");
    }

    @Override
    @Cacheable(cacheNames = "SeatService",
            key = "'findByRoom_'+#limit +'-'+#offset+'-'+#sort+" +
                    "'-'+#order+'-'+#room+'-'+#showTimes",unless = "#result == null")
    public List<SeatDto> findByRoom(Integer limit, Integer offset,String sort ,String order,
                                    Integer room,Integer showTimes) {
        var arrSeat = this.seatRepository.findAll(limit,offset,sort,order,null,room,this.userService.getTheaterByUser());
        var arrSeatAvailable = this.seatRepository.findSeatInRoomByShowTimesDetail(showTimes,room);
        return this.convertSeat(arrSeat,arrSeatAvailable,showTimes);
    }

    @Override
    public List<SeatDto> findByTheater(String date, String time, Integer theater, Integer movie) {
        var show = this.showTimesDetailService.findByTheaterMovieTime(movie,theater,date,time);
        var arrSeat = this.seatRepository.findAll(1000,0,"tier","ASC",null,
                show.getRoomId(),theater);
        var arrSeatAvailable = this.seatRepository.findSeatInRoomByShowTimesDetail(show.getId(),show.getRoomId());

        return this.convertSeat(arrSeat,arrSeatAvailable,show.getId());
    }

    private List<SeatDto> convertSeat(List<Seat> arrSeat,List<Seat> arrSeatAvailable,Integer showTime){
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
        return this.convertSeatToSeatHavePrice(arrSeat,showTime);
    }




    @Override
    public int countSeatAvailable(Integer show,Integer room) {
        return this.seatRepository.findSeatInRoomByShowTimesDetail(show,room).size();
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

    @Override
    @Cacheable(cacheNames = "SeatService",
            key = "'findAllSeatInShowTimeUnavailable_'+#showTimesId",unless = "#result == null")
    public List<Seat> findAllSeatInShowTimeUnavailable(Integer showTimesId) {
        return this.seatRepository.findAllSeatInShowTimeUnavailable(showTimesId);
    }

    @Override
    public ShowTimesDetail findShowTimesById(Integer id) {
        return this.showTimesDetailService.findById(id);
    }

    @Override
    public void sendDataToClient(Integer showTimesId) throws ExecutionException, InterruptedException {
        var showTimes = this.findShowTimesById(showTimesId);
        var seatMap = this.findByRoom(1000,0,"id","ASC",showTimes.getRoomId(),showTimesId);
        kafkaTemplate.send("test-websocket","seat-map",
                new com.apps.config.kafka.Message("seat",showTimes.getId(), Collections.singletonList(seatMap))).get();
    }

    @Override
    public double percentCoverRoom(Integer room) {



        return 0;
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
