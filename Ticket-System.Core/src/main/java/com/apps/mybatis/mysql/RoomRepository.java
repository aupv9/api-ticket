package com.apps.mybatis.mysql;

import com.apps.domain.entity.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomRepository {
//    @Options(useGeneratedKeys = true, keyProperty = "room.id", keyColumn = "id")
//    @Insert("INSERT INTO booksystem.room (code, name, theater_id,type)\n" +
//            "VALUES (#{room.code},#{room.name},#{room.theaterId},#{room.type})")
    int insert(@Param("room") Room room);

    int insert2(@Param("returnedId") Integer returnedId,@Param("room") Room room);

    List<Room> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                       @Param("sort") String sort, @Param("order") String order,
                       @Param("search") String search, @Param("theater") Integer theater);

    Room findById(@Param("id")Integer id);

    @Select("select * from room where code = #{code}")
    Room findByCode(@Param("code")String code);

    int update(@Param("room")Room room);
    void delete(@Param("id")Integer id);
    int findCountAll(@Param("search") String search, @Param("theater") Integer theater);

    @Select("select count(*) from seat where room_id = #{id}")
    int countSeatById(@Param("id")Integer room);

    @Select("select * from room where theater_id = #{theater}")
    List<Room> findByTheater(@Param("theater")Integer theater);
}
