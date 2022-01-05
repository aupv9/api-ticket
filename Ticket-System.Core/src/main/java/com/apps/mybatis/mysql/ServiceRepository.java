package com.apps.mybatis.mysql;

import com.apps.domain.entity.RoomService;
import com.apps.domain.entity.Service;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ServiceRepository {
    List<Service> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                          @Param("sort") String sort, @Param("order") String order,
                          @Param("search") String search);
    Service findById(@Param("id")Integer id);
    int update(@Param("service")Service service);
    int delete(@Param("id")Integer id);
    int insertRoomService(@Param("roomId")Integer roomId,@Param("serviceId")Integer serviceId);

    @Select("select * from room_service where room_id = #{room}")
    List<RoomService> findRoomServiceByRoom(@Param("room")Integer room);

    @Delete("delete from room_service where room_id = #{id}")
    int deleteByRoom(@Param("id")Integer id);
}
