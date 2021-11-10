package com.apps.mybatis.mysql;

import com.apps.domain.entity.Promotion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionRepository {

    List<Promotion> findAll(@Param("limit") int limit, @Param("limit")int offset,@Param("sort") String sort,@Param("order")String order,
                            @Param("startDate") String startDate, @Param("endDate")String endDate,@Param("displayName") String displayName,
                            @Param("creationDate") String creationDate, @Param("enable")boolean enable,@Param("beginUsable") String beginUsable,
                            @Param("endUsable") String endUsable, @Param("global")boolean global, @Param("anonProfile")boolean anonProfile,
                            @Param("promotionType") String promotionType,@Param("priority")int priority);
    int findAllCount(@Param("createdDate")String createdDate, @Param("endDate")String endDate,@Param("displayName") String displayName,
                     @Param("creationDate") String creationDate, @Param("enable")boolean enable,@Param("beginUsable") String beginUsable,
                     @Param("endUsable") String endUsable, @Param("global")boolean global, @Param("anonProfile")boolean anonProfile,
                     @Param("promotionType") String promotionType,@Param("priority")int priority);

}
