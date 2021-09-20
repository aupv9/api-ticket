package com.apps.service.impl;

import com.apps.domain.entity.ShowTimes;
import com.apps.mybatis.mysql.ShowTimesRepository;
import com.apps.service.ShowTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowTimesServiceImpl implements ShowTimesService {

    @Autowired
    private ShowTimesRepository showtimesRepository;

    @Override
    public List<ShowTimes> findAll(int page, int size) {
        return this.showtimesRepository.findAll(size, page * size);
    }

    @Override
    public int insert(ShowTimes showTimes) {
        return this.showtimesRepository.insert(showTimes);
    }

    @Override
    public ShowTimes findById(int id) {
        return this.showtimesRepository.findById(id);
    }
}
