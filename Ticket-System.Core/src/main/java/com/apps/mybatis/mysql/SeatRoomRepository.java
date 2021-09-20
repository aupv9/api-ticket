package com.apps.mybatis.mysql;

import com.apps.domain.entity.SeatDataRoom;
import com.apps.domain.entity.SeatRoom;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;


import java.util.List;

@Mapper
public interface SeatRoomRepository {
    List<SeatRoom> findAll(@Param("limit") int limit, @Param("offset") int offset);


    @Select("Select * from seat_room where id = #{id}")
    SeatRoom findById(@Param("id") int id);

    List<SeatRoom> findByAll(@Param("roomId") int roomId, @Param("showTimesDetailId") int showTimesDetailId,
                             @Param("seatId") int seatId, @Param("tierId") int tier);

    List<SeatDataRoom> findSeatInRoomByShowTimes(@Param("id") int showTimesId);

    @Update("call clockSeatRoom(#{id, mode = IN, jdbcType = INTEGER})")
    @Options(statementType = StatementType.CALLABLE)
    int reservingSeat(@Param("id") int id);

    int insert(@Param("seatRoom") SeatRoom seatRoom);
}
