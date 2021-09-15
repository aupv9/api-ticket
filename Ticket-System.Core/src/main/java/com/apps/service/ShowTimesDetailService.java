package com.apps.service;

import com.apps.domain.entity.ShowTimesDetail;

import java.util.List;

public interface ShowTimesDetailService {
    List<ShowTimesDetail> findAll(int page, int size);
    ShowTimesDetail findById(int id);
    int insert(ShowTimesDetail showTimesDetail);
}
