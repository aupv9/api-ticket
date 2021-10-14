package com.apps.service;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.response.TimePick;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

public interface ShowTimesDetailService {
    List<ShowTimesDetail> findAll(int page, int size, String sort, String order,
                                  Integer movieId, Integer roomid, String timeStart,String search,String dateStart);
    int findCountAll(
                     Integer movieId, Integer room_id, String time_start,String search,String dateStart);
    ShowTimesDetail findById(int id);
    int insert(ShowTimesDetail showTimesDetail) throws SQLException;
    List<ShowTimesDetail> findByShowTimes(int idShowTimes);
    List<List<ShowTimesDetailMini>> findShowTimesDetailByLocationAndDate(@Param("location") int location,
                                                                         @Param("date") String date);
    int update(ShowTimesDetail showTimesDetail) ;
    int countShowTimesDetailByShowTimes(int idShowTimes);
    List<TimePick> getTimeStart();
    void delete(Integer id);
}
