package com.apps.service;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.request.ShowTimeDto;
import com.apps.response.TimePick;
import com.apps.response.entity.ShowTimesDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

public interface ShowTimesDetailService {
    List<ShowTimesDetailDto> findAll(int limit, int offset, String sort, String order,
                                  Integer movieId, Integer roomid,String search,String dateStart,Integer theater,
            Boolean nowPlaying, Boolean comingSoon);

    List<ShowTimesDetailDto> findAllShow(int limit, int offset, String sort, String order,
                                      Integer movieId, Integer roomId,Integer theater,
                                      String search,String dateStart,Boolean nowPlaying,
                                         Boolean comingSoon
                                         );

    List<ShowTimesDetailDto> findAllByMovie(int page, int size, String sort, String order,
                                            Integer movieId, Integer roomId, String search, String dateStart,
                                            Integer theater);

    int findCountAll(Integer movieId, Integer room_id,String search,Integer theater,String dateStart,Boolean nowPlaying,
                     Boolean comingSoon);
    int findAllCountByMovie(Integer movieId,String search,String dateStart,Integer theater,Boolean nowPlaying,
                            Boolean comingSoon);

    int findCountAllShow(Integer movieId, Integer room_id,String search,Integer theater,String dateStart,Boolean nowPlaying,
                         Boolean comingSoon);

    ShowTimesDetail findById(int id);
    int insert(ShowTimeDto shoTimes) throws SQLException;
    int insert2(ShowTimesDetail shoTimes) throws SQLException;

    List<ShowTimesDetail> findByShowTimes(int idShowTimes);
    List<List<ShowTimesDetailMini>> findShowTimesDetailByLocationAndDate(@Param("location") int location,
                                                                         @Param("date") String date);
    int update(ShowTimesDetailDto showTimesDetail) ;
    int countShowTimesDetailByShowTimes(int idShowTimes);
    List<TimePick> getTimeStart();
    void delete(Integer id);

    List<Integer> findShowStartByDay(String date,Integer room);

}
