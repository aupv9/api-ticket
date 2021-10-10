package com.apps.service.impl;

import com.apps.domain.entity.ShowTimes;
import com.apps.domain.repository.ShowTimesCustomRepository;
import com.apps.mybatis.mysql.ShowTimesRepository;
import com.apps.service.ShowTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ShowTimesServiceImpl implements ShowTimesService {

    @Autowired
    private ShowTimesRepository showtimesRepository;

    @Autowired
    private ShowTimesCustomRepository showTimesCustomRepository;

    @Override
    @Cacheable(cacheNames = "ShowTimesService",key = "'ShowTimesList_'+#page +'-'+#size+'-'+#sort +'-'+#order")
    public List<ShowTimes> findAll(int page, int size, String sort, String order) {
        return this.showtimesRepository.findAll(size, page * size,sort, order);
    }

    @Override
    public int findCountAll() {
        return this.showtimesRepository.findAllCount();
    }

    @Override
    public int insert(ShowTimes showTimes) throws SQLException {
        String sql = "INSERT INTO SHOWTIMES(start_date,end_date,creation_date) " +
                "values(?,?,?)";
        return this.showTimesCustomRepository.insert(showTimes,sql);
    }

    @Override
    public int update(ShowTimes showTimes) {
        return 0;
    }

    @Override
    public ShowTimes findById(int id) {
        return this.showtimesRepository.findById(id);
    }
}
