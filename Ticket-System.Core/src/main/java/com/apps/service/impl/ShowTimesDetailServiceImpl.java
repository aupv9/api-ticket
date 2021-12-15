package com.apps.service.impl;


import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.repository.ShowTimesDetailsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.response.TimePick;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ShowTimesDetailServiceImpl implements ShowTimesDetailService {

    private final ShowTimesDetailRepository showTimesDetailRepository;

    private final ShowTimesDetailsCustomRepository repository;

    private final RoomService roomService;

    private final UserServiceImpl userService;


    @Override
    public List<ShowTimesDetail> findAll(int limit, int offset,String sort, String order,
                                         Integer movieId, Integer room_id,String search
                                        ,String dateStart,Integer theater,String currentTime) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isSeniorManager(userId);
        return isSeniorManager ? findAllSeniorManager(limit, offset,sort,order,movieId,room_id
                                                    ,search,dateStart, theater,currentTime) :
                findAllManager(limit, offset,sort,order,movieId,room_id,search,dateStart, theaterId,currentTime);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllSeniorManager_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public List<ShowTimesDetail> findAllSeniorManager(int limit, int offset,String sort, String order,
                                                    Integer movieId, Integer room_id,String search
                                                    ,String dateStart,Integer theater,String currentTime){
        return this.showTimesDetailRepository.findAll(limit,offset,sort,order,movieId > 0 ? movieId : null,room_id > 0 ? room_id : null,
                                                        search,dateStart, theater > 0 ? theater : null,currentTime);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public List<ShowTimesDetail> findAllManager(int limit, int offset,String sort, String order,
                                                Integer movieId, Integer room_id,String search
            ,String dateStart,Integer theater,String currentTime){
        return this.showTimesDetailRepository.findAll(limit, offset,sort,order,movieId > 0 ? movieId : null,room_id > 0 ? room_id : null
                ,search,dateStart, theater,currentTime);
    }


    @Override
    public int findCountAll( Integer movieId, Integer room_id,String search,Integer theater,
                             String dateStart,String currentTime) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isSeniorManager(userId);
        return isSeniorManager ? this.findCountAllSeniorManager(movieId,room_id,search, dateStart,theater,currentTime) :
                this.findCountAllManager(movieId,room_id,search, dateStart,theaterId,currentTime);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findCountAll_'+#movieId +'-'+#room_id+'-'+#search+'-'"+"+#dateStart+'-'+#theater+'-'+#currentTime",
            unless = "#result == null")
    public int findCountAllSeniorManager(Integer movieId, Integer room_id,String search,String dateStart,Integer theater,String currentTime){
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,room_id > 0 ? room_id : null, search,dateStart, theater > 0 ? theater : null,currentTime);
    }


    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public int findCountAllManager(Integer movieId, Integer room_id,String search,String dateStart,Integer theater,String currentTime){
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,room_id > 0 ? room_id : null,search,dateStart, theater,currentTime);
    }



    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllManager_'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public int findAllCountByMovie(Integer movieId, Integer roomId, String search, String dateStart, Integer theater, String currentTime) {
        return this.showTimesDetailRepository.findCountAllByMovie(movieId > 0 ? movieId : null,search,dateStart, theater,currentTime);
    }


    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllShow_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+" +
                    "'-'+#search+'-'+#movieId +'-'+#roomId+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public List<ShowTimesDetail> findAllShow(int limit, int offset, String sort, String order,
                                             Integer movieId, Integer roomId,Integer theater,
                                             String search,String dateStart,String currentTime){
        return this.showTimesDetailRepository.findAll(limit, offset,sort,order,
                movieId > 0 ? movieId : null,roomId > 0 ? roomId : null,search,dateStart,theater,
                currentTime);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findAllByMovie_'+#limit +'-'+#offset+'-'+#sort +'-'+#order+" +
                    "'-'+#search+'-'+#movieId +'-'+#roomId+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public List<ShowTimesDetail> findAllByMovie(int limit, int offset, String sort, String order, Integer movieId,
                                                Integer roomId, String search, String dateStart,Integer theater,String currentTime) {
        return this.showTimesDetailRepository.findAllByMovie(limit, offset, sort, order, movieId > 0 ? movieId : null,
                search, dateStart,theater > 0 ? theater : null,currentTime);
    }


    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'findCountAllShow_'+#search+'-'+#movieId +'-'+#roomId+'-'+#dateStart+'-'+#theater+'-'+#currentTime",unless = "#result == null")
    public int findCountAllShow(Integer movieId, Integer roomId,String search,Integer theater,String dateStart,String currentTime){
        int theaterId = this.userService.getTheaterByUser();
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,
                roomId > 0 ? roomId : null, search, dateStart, theaterId, currentTime);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key = "'findShowTimesDetailById_'+#id",unless = "#result == null")
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
    public int insert(ShowTimesDetail showTimesDetail) throws SQLException {
        String sql = "INSERT INTO showtimes_detail(movie_id,room_id,time_start,time_end) values(?,?,?,?)";
        return this.repository.insert(showTimesDetail,sql);
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

    @Override
    @CacheEvict(value = "ShowTimesDetailService",allEntries = true)
    public int update(ShowTimesDetail showTimesDetail) {
        ShowTimesDetail showTimesDetail1 = this.findById(showTimesDetail.getId());
        var time = showTimesDetail.getTimeStart().split(" ");
        if(time.length > 1){
            showTimesDetail1.setTimeStart(showTimesDetail.getTimeStart());
        }else{
            Instant instant = Instant.parse(showTimesDetail.getTimeStart());
            if(instant != null){
                Timestamp timestamp = Timestamp.from(instant);
                showTimesDetail1.setTimeStart(timestamp.toString());
            }
        }
        showTimesDetail1.setMovieId(showTimesDetail.getMovieId());
        showTimesDetail1.setRoomId(showTimesDetail.getRoomId());
        showTimesDetail1.setPrice(showTimesDetail.getPrice());
        int result = this.showTimesDetailRepository.update(showTimesDetail1);
        return result;
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
