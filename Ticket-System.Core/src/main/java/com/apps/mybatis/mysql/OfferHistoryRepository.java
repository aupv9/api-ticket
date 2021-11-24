package com.apps.mybatis.mysql;

import com.apps.domain.entity.OfferHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OfferHistoryRepository {

    List<OfferHistory> findAll(@Param("limit")Integer limit, @Param("offset")Integer offset,
                               @Param("sort")String sort,
                               @Param("order")String order,@Param("userId")Integer userId,
                               @Param("offerId")Integer offerId,@Param("status")String status,
                               @Param("timeUsed")String timeUsed, @Param("orderId") Integer orderId
            , @Param("search")String search);
    int findAllCount(@Param("userId")Integer userId,
                     @Param("offerId")Integer offerId,@Param("status")String status,
                     @Param("timeUsed")String timeUsed, @Param("orderId") Integer orderId, @Param("search")String search);

    @Select("select * from offer_history where order_id = #{orderId}")
    OfferHistory findByOrder(@Param("orderId") int orderId);

    @Select("select * from offer_history where id = #{id}")
    OfferHistory findById(@Param("id")int id);
}
