package com.apps.mybatis.mysql;

import com.apps.domain.entity.Seat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeatRepository {

    List<Seat> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                       @Param("sort") String sort, @Param("order") String order,
                       @Param("search") String search, @Param("room") Integer room,
                       @Param("theater")Integer theater);


    @Select("SELECT * FROM SEAT WHERE ID = #{id}")
    Seat findById(Integer id);

    List<Seat> findSeatInRoomByShowTimesDetail(@Param("showTimesId") Integer showTimesDetailId, @Param("roomId") Integer roomId);

    int findCountAll(@Param("search") String search, @Param("room") Integer room,
                     @Param("theater")Integer theater);

    @Select("Select * from seat where room_id =#{room} ")
    List<Seat> findByRoom(@Param("room") Integer room);


    @Select("SELECT * FROM SEAT WHERE ROOM_ID = #{room} and TIER = #{tier} and numbers = #{numbers}")
    Seat validateSeat(@Param("room") Integer room, @Param("tier") String tier, @Param("numbers") Integer numbers);

    int update(@Param("seat") Seat seat);

    @Delete("DELETE FROM SEAT WHERE ID = #{id}")
    void delete(@Param("id") Integer id);

    @Select(" select seat_id as id from orders\n" +
            "             join orders_seat os on orders.id = os.orders_id\n" +
            "                                 where showtimes_detail_id = #{showTimeId};")
    List<Seat> findAllSeatInShowTimeUnavailable(@Param("showTimeId") Integer showTimesId);
}

