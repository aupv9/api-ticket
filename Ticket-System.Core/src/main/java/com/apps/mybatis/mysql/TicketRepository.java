package com.apps.mybatis.mysql;

import com.apps.domain.entity.Reserved;
import com.apps.mapper.ReservedDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketRepository {
    int reserved(@Param("seat")Integer seat,@Param("user")Integer user,
                 @Param("showtime")Integer showtime,@Param("room")Integer room);

    @Select("select count(*) from reserved where seat_id =#{seat} and show_time_id = #{showtime} and room_id = #{room}")
    int isReserved(@Param("seat")Integer seat, @Param("showtime")Integer showtime,@Param("room")Integer room);
    Reserved findSeatReserved(@Param("seat")Integer seat, @Param("showtime")Integer showtime,@Param("room")Integer room);

    @Select("select seat_id as seat, user_id as user, show_time_id as showtime, room_id as room from reserved where show_time_id = #{showtime} and room_id = #{room}")
    List<ReservedDto> findByRoomShowTime(@Param("room") Integer room, @Param("showtime") Integer showTime);

}
