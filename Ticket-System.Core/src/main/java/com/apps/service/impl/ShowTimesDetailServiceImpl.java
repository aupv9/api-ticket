package com.apps.service.impl;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.repository.ShowTimesDetailsCustomRepository;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.service.ShowTimesDetailService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

    @Override
//    @Cacheable(cacheNames = "ShowTimesDetailService",
//            key = "'ShowTimesDetailList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#movieId +'-'+#room_id+'-'+#time_start +'-'+#search")
    public List<ShowTimesDetail> findAll(int page, int size,String sort, String order,
                                         Integer movieId, Integer room_id, String time_start,String search
            ,String dateStart) {
        return this.showTimesDetailRepository.findAll(size, page * size
        ,sort,order,movieId,room_id,time_start,search,dateStart);
    }

    @Override
    public int findCountAll( Integer movieId, Integer room_id, String time_start,String search,String dateStart) {
        return this.showTimesDetailRepository.findCountAll(movieId,room_id,time_start,search, dateStart);
    }

    @Override
    public ShowTimesDetail findById(int id) {
        return this.showTimesDetailRepository.findById(id);
    }

    @Override
    public int insert(ShowTimesDetail showTimesDetail) throws SQLException {
        String sql = "INSERT INTO showtimes_detail(movie_id,room_id,time_start,time_end,dayshowtimes) values(?,?,?,?,?,?)";
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
    public int countShowTimesDetailByShowTimes(int idShowTimes) {
        return this.showTimesDetailRepository.countShowTimesDetailByShowTimes(idShowTimes);
    }

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key ="'getTimeStart'")
    public List<String> getTimeStart() {
        return this.showTimesDetailRepository.getTimeStart();
    }

}
