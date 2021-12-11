package com.apps.mybatis.mysql;

import com.apps.domain.entity.ShowTimesDetail;
import com.apps.domain.entity.ShowTimesDetailMini;
import com.apps.response.TimePick;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShowTimesDetailRepository {
    List<ShowTimesDetail> findAll(@Param("limit") int limit, @Param("offset") int offset,
                                  @Param("sort")String sort, @Param("order") String order, @Param("movieId")Integer movieId,
                                  @Param("roomId")Integer roomId,
                                  @Param("search")String search, @Param("dateStart") String dateStart,
                                  @Param("theaterId")Integer theaterId,@Param("currentTime")String currentTime);

    int findCountAll(@Param("movieId")Integer movieId, @Param("roomId")Integer roomId,
                      @Param("search")String search, @Param("dateStart") String dateStart,
                      @Param("theaterId")Integer theaterId,@Param("currentTime")String currentTime);



    List<ShowTimesDetail> findAllByMovie(@Param("limit") int limit, @Param("offset") int offset,
                                  @Param("sort")String sort, @Param("order") String order, @Param("movieId")Integer movieId,
                                         @Param("search")String search, @Param("dateStart") String dateStart,
                                  @Param("theaterId")Integer theaterId,@Param("currentTime")String currentTime);

    int findCountAllByMovie( @Param("movieId")Integer movieId,
                             @Param("search")String search, @Param("dateStart") String dateStart,
                             @Param("theaterId")Integer theaterId,@Param("currentTime")String currentTime);


    @Select("Select * from showtimes_detail where id = #{id}")
    ShowTimesDetail findById(int id);

    int insert(@Param("entity") ShowTimesDetail showTimesDetail);

    List<ShowTimesDetail> findByShowTimes(@Param("limit") int limit);

    List<ShowTimesDetailMini> findShowTimesDetailByLocationAndDate(@Param("location") int location,
                                                                   @Param("date") String date);
    @Select("Select count(*) from showtimes_detail  where id = #{id}")
    int countShowTimesDetailByShowTimes(@Param("id") int idShowTimes);

    List<TimePick> getTimeStart();

    int update(@Param("entity") ShowTimesDetail showTimesDetail) ;

    void delete(@Param("id") Integer id);

}
