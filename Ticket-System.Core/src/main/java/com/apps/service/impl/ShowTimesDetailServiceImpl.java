package com.apps.service.impl;


import com.apps.contants.Utilities;
import com.apps.domain.entity.Movie;
import com.apps.domain.entity.Room;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.repository.ShowTimesDetailsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.SeatRepository;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.request.RoomDto;
import com.apps.request.ShowTimeDto;
import com.apps.response.TimePick;
import com.apps.response.entity.ShowTimesDetailDto;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ShowTimesDetailServiceImpl implements ShowTimesDetailService {

    private final ShowTimesDetailRepository showTimesDetailRepository;

    private final ShowTimesDetailsCustomRepository repository;

    private final RoomService roomService;

    private final UserServiceImpl userService;

    private final MovieService movieService;

    private final SeatRepository seatRepository;

    @Override
    public List<ShowTimesDetailDto> findAll(int limit, int offset,String sort, String order,
                                         Integer movieId, Integer room_id,String search
                                        ,String dateStart,Integer theater,Boolean nowPlaying,
                                            Boolean comingSoon) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isSeniorManager(userId);
        return isSeniorManager ? this.addCountSeat(findAllSeniorManager(limit, offset,sort,order,movieId,room_id
                ,search,dateStart,theaterId,nowPlaying, comingSoon)) :
                this.addCountSeat(findAllManager(limit, offset,sort,order,movieId,room_id,search,dateStart,
                        theaterId,nowPlaying, comingSoon));
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllSeniorManager_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon",unless = "#result == null")
    public List<ShowTimesDetailDto> findAllSeniorManager(Integer limit, Integer offset,String sort, String order,
                                                    Integer movieId, Integer room_id,String search
                                                    ,String dateStart,Integer theater,Boolean nowPlaying,
                                                         Boolean comingSoon){
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findAll(limit,offset,sort,order,movieId > 0 ? movieId : null,room_id > 0 ? room_id : null,
                                                        search,dateStart, theater > 0 ? theater : null,minDate,maxDate);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon ",unless = "#result == null")
    public List<ShowTimesDetailDto> findAllManager(int limit, int offset,String sort, String order,
                                                Integer movieId, Integer room_id,String search
            ,String dateStart,Integer theater,Boolean nowPlaying, Boolean comingSoon){
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findAll(limit, offset,sort,order,movieId > 0 ? movieId : null,room_id > 0 ? room_id : null
                ,search,dateStart, theater,minDate,maxDate);
    }

    private List<ShowTimesDetailDto> addCountSeat(List<ShowTimesDetailDto> showTimes){
        return showTimes.stream()
                .peek(item -> item.setCountSeatAvailable(this.seatRepository.
                        findSeatInRoomByShowTimesDetail(item.getId(),item.getRoomId()).size()))
                .collect(Collectors.toList());
    }


    @Override
    public int findCountAll(Integer movieId, Integer room_id,String search,Integer theater,
                             String dateStart,Boolean nowPlaying,
                            Boolean comingSoon) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isSeniorManager(userId);
        return isSeniorManager ? this.findCountAllSeniorManager(movieId,room_id,search, dateStart,theater,
                nowPlaying,comingSoon) :
                this.findCountAllManager(movieId,room_id,search, dateStart,theaterId,nowPlaying,comingSoon);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findCountAll_'+#movieId +'-'+#room_id+'-'+#search+'-'"+
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon ",
            unless = "#result == null")
    public int findCountAllSeniorManager(Integer movieId, Integer room_id,String search,
                                         String dateStart,Integer theater,Boolean nowPlaying,
                                         Boolean comingSoon){
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,room_id > 0 ? room_id : null,
                search,dateStart, theater > 0 ? theater : null,minDate,maxDate);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon ",unless = "#result == null")
    public int findCountAllManager(Integer movieId, Integer room_id,String search,
                                   String dateStart,Integer theater,Boolean nowPlaying,
                                   Boolean comingSoon){
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,
                room_id > 0 ? room_id : null,search,dateStart, theater,minDate,maxDate);
    }



    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#movieId+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon ",unless = "#result == null")
    public int findAllCountByMovie(Integer movieId, String search, String dateStart, Integer theater,Boolean nowPlaying,
                                   Boolean comingSoon) {
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.startOfWeek("yyyy-MM-dd");
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(48,Utilities.startOfWeek("yyyy-MM-dd"));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findCountAllByMovie(movieId > 0 ? movieId : null,
                search,dateStart, theater,minDate,maxDate);
    }


    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllShow_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+" +
                    "'-'+#search+'-'+#movieId +'-'+#roomId+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon",unless = "#result == null")
    public List<ShowTimesDetailDto> findAllShow(int limit, int offset, String sort, String order,
                                                Integer movieId, Integer roomId, Integer theater,
                                                String search, String dateStart,Boolean nowPlaying,
                                                Boolean comingSoon){
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.addCountSeat(this.showTimesDetailRepository.findAll(limit, offset,sort,order,
                movieId > 0 ? movieId : null,roomId > 0 ? roomId : null,search,dateStart,theater,minDate,maxDate));
    }



    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllByMovie_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+" +
                    "'-'+#search+'-'+#movieId +'-'+#roomId+'-'" +
                    "+#dateStart+'-'+#theater",unless = "#result == null")
    public List<ShowTimesDetailDto> findAllByMovie(int limit, int offset, String sort, String order,
                                                   Integer movieId,
                                                Integer roomId, String search, String dateStart,
                                                   Integer theater) {
        return this.addCountSeat(this.showTimesDetailRepository.findAllByMovie(limit, offset, sort, order, movieId > 0 ? movieId : null,
                search, dateStart,theater > 0 ? theater : null));
    }


    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findCountAllShow_'+#search+'-'+#movieId +'-'+#roomId+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#nowPlaying+'-'+#comingSoon ",unless = "#result == null")
    public int findCountAllShow(Integer movieId, Integer roomId,String search,
                                Integer theater,String dateStart,Boolean nowPlaying,
                                Boolean comingSoon){
        int theaterId = this.userService.getTheaterByUser();
        String minDate = null,maxDate = null;
        if(nowPlaying){
            minDate = Utilities.subDate(42);
            maxDate = Utilities.addDateParam(48,minDate);
        }
        if(comingSoon){
            minDate = Utilities.addDateParam(49,Utilities.subDate(42));
            maxDate = Utilities.addDateParam(18,minDate);
        }
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,
                roomId > 0 ? roomId : null, search, dateStart, theaterId,minDate,maxDate);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findShowTimesDetailById_'+#id",unless = "#result == null")
    public ShowTimesDetail findById(int id) {
        var showTimesDetail = this.showTimesDetailRepository.findById(id);
        if(showTimesDetail == null){
            throw new NotFoundException("Not Found Object have Id:" + id);
        }
        var room = this.roomService.findById(showTimesDetail.getRoomId());
        if(room == null){
            throw new NotFoundException("Not Found Object have Id:" + id);
        }
        showTimesDetail.setTheaterId(room.getTheaterId());
        showTimesDetail.setRoomName(room.getName());
        return showTimesDetail;
    }

    @Override
    @CacheEvict(value = "ShowTimesDetailService",allEntries = true)
    public int insert(ShowTimeDto showTimesDetail) throws SQLException {
        String sql = "INSERT INTO showtimes_detail(movie_id,room_id,time_start,time_end,price) values(?,?,?,?,?)";

        var show = ShowTimesDetail.builder()
                .timeStart(showTimesDetail.getStart())
                .timeEnd(showTimesDetail.getEnd())
                .price(showTimesDetail.getPrice())
                .build();
        var room = this.roomService.findByCode(showTimesDetail.getRoom());
        if(room != null){
            show.setRoomId(room.getId());
        }
        var movie = this.movieService.findByName(showTimesDetail.getMovie());
        if(movie != null){
            show.setMovieId(movie.getId());
        }
        return this.repository.insert(show,sql);
    }

    @Override
    public int insert2(ShowTimesDetail showTimes) throws SQLException {
        String sql = "INSERT INTO showtimes_detail(movie_id,room_id,time_start,time_end,price) values(?,?,?,?,?)";
        Instant instant = Instant.parse(showTimes.getTimeStart());
        if(instant != null){
            Timestamp timestamp = Timestamp.from(instant);
            showTimes.setTimeStart(timestamp.toString());
        }
        instant = Instant.parse(showTimes.getTimeEnd());
        if(instant != null){
            Timestamp timestamp = Timestamp.from(instant);
            showTimes.setTimeEnd(timestamp.toString());
        }
        return this.repository.insert(showTimes,sql);
    }

    @Override
    public List<ShowTimesDetail> findByShowTimes(int idShowTimes) {
        return this.showTimesDetailRepository.findByShowTimes(idShowTimes);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findShowTimesDetailByLocationAndDate_'+#location +'-'+#date",unless = "#result == null")
    public List<List<ShowTimesDetailMini>> findShowTimesDetailByLocationAndDate(int location, String date) {
        Map<Integer,List<ShowTimesDetailMini>> map = new HashMap<>();
        var list = this.showTimesDetailRepository.findShowTimesDetailByLocationAndDate(location,date);
        for (var item:list){
            var key = item.getTheaterId();
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
                map.get(key).add(item);
            }
            map.get(key).add(item);
        }
        var listResult = new ArrayList<List<ShowTimesDetailMini>>();
        for (var item: map.entrySet()){
            listResult.add(item.getValue());
        }
        return listResult;
    }

    private String convertDate(String date){
        var time = date.split(" ");
        if(time.length > 1){
            return date;
        }else{
            Instant instant = Instant.parse(date);
            if(instant != null){
                return Timestamp.from(instant).toString();
            }
        }
        return date;
    }

    @Override
    @CacheEvict(value = "ShowTimesDetailService",allEntries = true)
    public int update(ShowTimesDetailDto showTimesDetail) {
        ShowTimesDetail showTimesDetail1 = this.findById(showTimesDetail.getId());
        showTimesDetail1.setTimeStart(convertDate(showTimesDetail.getTimeStart()));
        showTimesDetail1.setTimeEnd(convertDate(showTimesDetail.getTimeEnd()));
        showTimesDetail1.setMovieId(showTimesDetail.getMovieId());
        showTimesDetail1.setRoomId(showTimesDetail.getRoomId());
        showTimesDetail1.setPrice(showTimesDetail.getPrice());
        showTimesDetail1.setStatus(showTimesDetail.getStatus());
        showTimesDetail1.setReShow(showTimesDetail.isReShow());
        return this.showTimesDetailRepository.update(showTimesDetail1);
    }

    @Override
    public int countShowTimesDetailByShowTimes(int idShowTimes) {
        return this.showTimesDetailRepository.countShowTimesDetailByShowTimes(idShowTimes);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key ="'getTimeStart'",unless = "#result == null")
    public List<TimePick> getTimeStart() {
        return this.showTimesDetailRepository.getTimeStart();
    }

    @Override
    @CacheEvict(value = "ShowTimesDetailService",allEntries = true)
    public void delete(Integer id) {
        var showTimesDetail = this.findById(id);
        this.showTimesDetailRepository.delete(id);
    }

}
