package com.apps.service;


import com.apps.domain.entity.ShowTimes;

import java.util.List;

public interface ShowTimesService {
    List<ShowTimes> findAll(int page, int size);
    int insert(ShowTimes showTimes);
    ShowTimes findById(int id);
}
