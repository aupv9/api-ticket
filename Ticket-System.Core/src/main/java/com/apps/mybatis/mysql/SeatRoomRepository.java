package com.apps.mybatis.mysql;

import com.apps.domain.entity.SeatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeatRoomRepository {
    List<SeatRoom> findAll(@Param("limit") int limit, @Param("offset") int offset);

    @Select("Select * from seat_room where id = #{id}")
    SeatRoom findById(@Param("id") int id);

    List<SeatRoom> findByAll(@Param("roomId") int roomId, @Param("showTimesDetailId") int showTimesDetailId,
                             @Param("seatId") int seatId, @Param("tierId") int tier);


    int reservedSeat(@Param("id") int id,@Param("status") boolean status);


}
