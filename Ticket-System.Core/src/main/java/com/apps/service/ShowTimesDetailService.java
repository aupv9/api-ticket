package com.apps.service;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.response.TimePick;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

public interface ShowTimesDetailService {
    List<ShowTimesDetail> findAll(int limit, int offset, String sort, String order,
                                  Integer movieId, Integer roomid,String search,String dateStart,Integer theater,
                                  String currentTime);

    List<ShowTimesDetail> findAllShow(int limit, int offset, String sort, String order,
                                      Integer movieId, Integer roomId,Integer theater,
                                      String search,String dateStart,String currentTime);

    List<ShowTimesDetail> findAllByMovie(int page, int size, String sort, String order,
                                  Integer movieId, Integer roomId,String search,String dateStart,Integer theater,String currentTime);

    int findCountAll(Integer movieId, Integer room_id,String search,Integer theater,String dateStart,String currentTime);
    int findAllCountByMovie(Integer movieId, Integer roomid,String search,String dateStart,Integer theater,String currentTime);

    int findCountAllShow(Integer movieId, Integer room_id,String search,Integer theater,String dateStart,String currentTime);

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
