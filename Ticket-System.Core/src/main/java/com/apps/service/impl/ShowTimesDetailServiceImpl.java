package com.apps.service.impl;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.service.ShowTimesDetailService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShowTimesDetailServiceImpl implements ShowTimesDetailService {

    @Autowired
    private ShowTimesDetailRepository showTimesDetailRepository;

    @Override
    @Cacheable(cacheNames = "ShowTimesDetailService",key = "'ShowTimesDetailList_'+#page +'-'+#size")
    public List<ShowTimesDetail> findAll(int page, int size) {
        return this.showTimesDetailRepository.findAll(size, page * size);
    }

    @Override
    public ShowTimesDetail findById(int id) {
        return this.showTimesDetailRepository.findById(id);
    }

    @Override
    public int insert(ShowTimesDetail showTimesDetail) {
        return this.showTimesDetailRepository.insert(showTimesDetail);
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

}
