package com.apps.service.impl;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.mybatis.mysql.ShowTimesDetailRepository;
import com.apps.service.ShowTimesDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShowTimesDetailServiceImpl implements ShowTimesDetailService {

    @Autowired
    private ShowTimesDetailRepository showTimesDetailRepository;

    @Override
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
}
