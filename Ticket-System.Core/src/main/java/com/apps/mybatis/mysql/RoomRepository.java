package com.apps.mybatis.mysql;

import com.apps.domain.entity.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomRepository {
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO booksystem.room (code, name, theater_id)\n" +
            "VALUES (#{room.code},#{room.name},#{room.theaterId})")
    int insert(@Param("room") Room room);
    List<Room> findAll(@Param("limit") int limit, @Param("offset") int offset);

}
