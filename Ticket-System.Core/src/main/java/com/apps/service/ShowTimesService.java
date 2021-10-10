package com.apps.service;


import com.apps.domain.entity.ShowTimes;

import java.sql.SQLException;
import java.util.List;

public interface ShowTimesService {
    List<ShowTimes> findAll(int page, int size, String sort, String order);
    int findCountAll();
    int insert(ShowTimes showTimes) throws SQLException;
    int update(ShowTimes showTimes);
    ShowTimes findById(int id);
}
