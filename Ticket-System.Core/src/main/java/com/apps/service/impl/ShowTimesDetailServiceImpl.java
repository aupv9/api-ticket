package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.contants.Utilities;
import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.repository.ShowTimesDetailsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.response.TimePick;
import com.apps.service.EmployeeService;
import com.apps.service.RoomService;
import com.apps.service.ShowTimesDetailService;
import com.apps.service.UserService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShowTimesDetailServiceImpl implements ShowTimesDetailService {

    @Autowired
    private ShowTimesDetailRepository showTimesDetailRepository;

    @Autowired
    private ShowTimesDetailsCustomRepository repository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ApplicationCacheManager cacheManager;


    @Override
//    @Cacheable(cacheNames = "ShowTimesDetailService",
//            key = "'ShowTimesDetailList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#time_start +'-'+#search")
    public List<ShowTimesDetail> findAll(int page, int size,String sort, String order,
                                         Integer movieId, Integer room_id, String time_start,String search
            ,String dateStart) {
        return this.showTimesDetailRepository.findAll(size, page * size,sort,order,movieId,room_id,time_start,search,dateStart,null,null);
    }

    @Override
    public int findCountAll( Integer movieId, Integer room_id, String time_start,String search,String dateStart) {
        return this.showTimesDetailRepository.findCountAll(movieId,room_id,time_start,search, dateStart, null,null);
    }
    private int getTheaterByUser(){
        var authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails)authentication.getPrincipal();
        int userId = 0;
        if(userDetails != null){
            String email = userDetails.getUsername();
            var user = this.userAccountRepository.findUserByEmail(email);
            if( user!= null){
                userId = user.getId();
            }else{
                var userInfo1 = this.userAccountRepository.findUserInfoByEmail(email);
                if(userInfo1 != null){
                    userId = userInfo1.getId();
                }
            }
        }
        int theaterId = 0;
        if(userId > 0){
            var employee = this.employeeService.findByUserId(userId);
            theaterId = employee.getTheaterId();
        }
        return theaterId;
    }

    @Override
    public List<ShowTimesDetail> findAllShow(int page, int size, String sort, String order, Integer movieId, Integer roomid, String timeStart,String search,String dateStart){
        int theaterId = this.getTheaterByUser();
        return this.showTimesDetailRepository.findAll(size, page * size,sort,order,movieId > 0 ? movieId : null,roomid > 0 ? roomid : null,timeStart,search,dateStart,theaterId,
                Utilities.getCurrentTime());
    }

    @Override
    public int findCountAllShow(Integer movieId, Integer roomid, String time_start,String search,String dateStart){
        int theaterId = this.getTheaterByUser();
        return this.showTimesDetailRepository.findCountAll(movieId > 0 ? movieId : null,roomid > 0 ? roomid : null,time_start,search, dateStart, theaterId,Utilities.getCurrentTime());
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
        var timeStart = Timestamp.valueOf(showTimesDetail1.getTimeStart()).toString();
        showTimesDetail1.setTimeStart(timeStart);
//        if(showTimesDetail1.getTimeStart() != null && !showTimesDetail1.getTimeStart().isEmpty()){
//            Instant instantFromTimeStamp = Timestamp.valueOf(showTimesDetail1.getTimeStart()).toInstant();
//
//            if(!instantFromTimeStamp.toString().equals(showTimesDetail.getTimeStart())){
//                Instant instant = Instant.parse(showTimesDetail.getTimeStart());
//                Timestamp timestamp = Timestamp.from(instant);
//                showTimesDetail1.setTimeStart(timestamp.toString());
//            }
//        }else{
//            Instant instant = Instant.parse(showTimesDetail.getTimeStart());
//            Timestamp timestamp = Timestamp.from(instant);
//            showTimesDetail1.setTimeStart(timestamp.toString());
//        }

        showTimesDetail1.setMovieId(showTimesDetail.getMovieId());
        showTimesDetail1.setRoomId(showTimesDetail.getRoomId());
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
