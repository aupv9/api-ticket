package com.apps.mybatis.mysql;

import com.apps.domain.entity.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceRepository {
    List<Service> findAll(@Param("limit") Integer limit, @Param("offset") Integer offset,
                          @Param("sort") String sort, @Param("order") String order,
                          @Param("search") String search);
    Service findById(@Param("id")Integer id);
    int update(@Param("service")Service service);
    int delete(@Param("id")Integer id);
    int insertRoomService(@Param("roomId")Integer roomId,@Param("serviceId")Integer serviceId);
}
