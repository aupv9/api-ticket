package com.apps.mybatis.mysql;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.domain.entity.Theater;
import com.apps.response.TimePick;
import com.apps.response.entity.ShowTimesDetailDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShowTimesDetailRepository {
    List<ShowTimesDetailDto> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                                     @Param("sort")String sort, @Param("order") String order,
                                     @Param("movieId")Integer movieId, @Param("roomId")Integer roomId,
                                     @Param("search")String search, @Param("dateStart") String dateStart,
                                     @Param("theaterId")Integer theaterId,@Param("minDate")String minDate,
                                     @Param("maxDate") String maxDate);

    List<ShowTimesDetailDto> findByMovieAndTime(@Param("theaterId")Integer theater,@Param("movieId")Integer movie,
                                                @Param("date")String date);



    ShowTimesDetailDto findByTheaterAndMovieTime(@Param("theaterId")Integer theater,@Param("movieId")Integer movie,
                                                @Param("date")String date,@Param("time")String time);


    int findCountAll(@Param("movieId")Integer movieId, @Param("roomId")Integer roomId,
                      @Param("search")String search, @Param("dateStart") String dateStart,
                      @Param("theaterId")Integer theaterId,@Param("minDate")String minDate,
                      @Param("maxDate") String maxDate);


    List<String> findTimeByMovieTheaterDate(@Param("theaterId")Integer theater,@Param("movieId")Integer movie,
                                            @Param("date")String date);

    List<ShowTimesDetailDto> findAllByMovie(@Param("limit") Integer limit, @Param("offset") Integer offset,
                                            @Param("sort")String sort, @Param("order") String order,
                                            @Param("movieId")Integer movieId, @Param("search")String search,
                                            @Param("dateStart") String dateStart,
                                            @Param("theaterId")Integer theaterId);

    int findCountAllByMovie( @Param("movieId")Integer movieId,
                             @Param("search")String search, @Param("dateStart") String dateStart,
                             @Param("theaterId")Integer theaterId,@Param("minDate")String minDate,
                             @Param("maxDate") String maxDate);


    @Select("Select * from showtimes_detail where id = #{id}")
    ShowTimesDetail findById(int id);

//    @Insert("insert into table2 (name) values(#{name})")
//    @SelectKey(statement = "call identity()", keyProperty = "nameId", before = false, resultType = int.class)
    int insert(@Param("entity") ShowTimesDetail showTimesDetail);

    List<ShowTimesDetail> findByShowTimes(@Param("limit") int limit);

    List<ShowTimesDetailMini> findShowTimesDetailByLocationAndDate(@Param("location") int location,
                                                                   @Param("date") String date);
    @Select("Select count(*) from showtimes_detail  where id = #{id}")
    int countShowTimesDetailByShowTimes(@Param("id") int idShowTimes);

    List<TimePick> getTimeStart();

    int update(@Param("entity") ShowTimesDetail showTimesDetail) ;

    void delete(@Param("id") Integer id);

    List<ShowTimesDetail> findShowStartByDay(@Param("date") String date,@Param("room") Integer room);


    List<Theater> findCinemasByMovie(@Param("movie")Integer movie);

    @Select("Select max(id) from showtimes_detail")
    int selectMaxKey();



}
