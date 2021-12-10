package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.Role;
import com.apps.contants.Utilities;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.repository.ShowTimesDetailsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.response.TimePick;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
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

    private final ApplicationCacheManager cacheManager;

    private final UserServiceImpl userService;



    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'ShowTimesDetailList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime")
    public List<ShowTimesDetail> findAll(int page, int size,String sort, String order,
                                         Integer movieId, Integer room_id,String search
                                        ,String dateStart,Integer theater,String currentTime) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isManager(userId);

        return this.showTimesDetailRepository.findAll(size, page * size,sort,order,movieId,room_id
                ,search,dateStart, isSeniorManager ? theater > 0 ? theater : null : theaterId,currentTime);
    }


    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",
            key = "'ShowTimesDetailListCount_'+#movieId +'-'+#room_id+'-'+#search+'-'" +
                    "+#dateStart+'-'+#theater+'-'+#currentTime")
    public int findCountAll( Integer movieId, Integer room_id,String search,Integer theater,String dateStart,String currentTime) {
        var userId = this.userService.getUserFromContext();
        var theaterId = this.userService.getTheaterManagerByUser(userId);
        var isSeniorManager = this.userService.isManager(userId);
        return this.showTimesDetailRepository.findCountAll(movieId,room_id,search, dateStart,
                isSeniorManager ? theater > 0 ? theater : null : theaterId,currentTime);
    }

    @Override
    public int findAllCountByMovie(Integer movieId, Integer roomId, String search, String dateStart, Integer theater, String currentTime) {
        return 0;
    }


    @Override
    public List<ShowTimesDetail> findAllShow(int page, int size, String sort, String order,
                                             Integer movieId, Integer roomId,Integer theater,
                                             String search,String dateStart,String currentTime){
        return this.showTimesDetailRepository.findAll(size, page * size,sort,order,
                movieId > 0 ? movieId : null,roomId > 0 ? roomId : null,search,dateStart,theater,
                currentTime);
    }

    @Override
    public List<ShowTimesDetail> findAllByMovie(int page, int size, String sort, String order, Integer movieId, Integer roomid,
                                                String search, String dateStart,Integer theater,String currentTime) {
        return this.showTimesDetailRepository.findAllByMovie(page, size, sort, order, movieId > 0 ? movieId : null,
                search, dateStart,theater > 0 ? theater : null,currentTime);
    }

    @Override
    public int findCountAllShow(Integer movieId, Integer roomId,String search,Integer theater,String dateStart,String currentTime){
        int theaterId = this.userService.getTheaterByUser();
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,
                roomId > 0 ? roomId : null, search, dateStart, theaterId, currentTime);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key = "'findByIdShowTimesDetail_'+#id")
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
    public int insert(ShowTimesDetail showTimesDetail) throws SQLException {
        String sql = "INSERT INTO showtimes_detail(movie_id,room_id,time_start,time_end) values(?,?,?,?)";
        return this.repository.insert(showTimesDetail,sql);
    }

    @Override
    public List<ShowTimesDetail> findByShowTimes(int idShowTimes) {
        return this.showTimesDetailRepository.findByShowTimes(idShowTimes);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key = "'findShowTimesDetailByLocationAndDate_'+#location +'-'+#date")
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
        cacheManager.clearCache("ShowTimesDetailService");
        return result;
    }

    @Override
    public int countShowTimesDetailByShowTimes(int idShowTimes) {
        return this.showTimesDetailRepository.countShowTimesDetailByShowTimes(idShowTimes);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key ="'getTimeStart'")
    public List<TimePick> getTimeStart() {
        return this.showTimesDetailRepository.getTimeStart();
    }

    @Override
    public void delete(Integer id) {
        var showTimesDetail = this.findById(id);
        this.showTimesDetailRepository.delete(id);
    }

}
