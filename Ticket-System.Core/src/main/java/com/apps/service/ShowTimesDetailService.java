package com.apps.service;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShowTimesDetailService {
    List<ShowTimesDetail> findAll(int page, int size);
    ShowTimesDetail findById(int id);
    int insert(ShowTimesDetail showTimesDetail);
    List<ShowTimesDetail> findByShowTimes(int idShowTimes);
    List<List<ShowTimesDetailMini>> findShowTimesDetailByLocationAndDate(@Param("location") int location,
                                                                         @Param("date") String date);
}
