package com.apps.mybatis.mysql;

import com.apps.domain.entity.Offer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PromotionRepository {

    List<Offer> findAll(@Param("limit") int limit, @Param("offset")int offset, @Param("sort") String sort, @Param("order")String order,
                        @Param("startDate") String startDate, @Param("endDate")String endDate, @Param("creationBy") Integer creationBy,
                        @Param("creationDate") String creationDate,@Param("anonProfile")boolean anonProfile,
                        @Param("type") String promotionType, @Param("method")String method,@Param("multi")boolean multi,
                         @Param("search")String search);

    int findAllCount(@Param("startDate") String startDate, @Param("endDate")String endDate, @Param("creationBy") Integer creationBy,
                     @Param("creationDate") String creationDate,@Param("anonProfile")boolean anonProfile,
                     @Param("type") String promotionType, @Param("method")String method,@Param("multi")boolean multi,
                     @Param("search")String search);

    @Insert("insert into offer_detail(offer_id,code) values(#{offerId},#{code})")
    int insertOfferDetail(@Param("offerId")int offerId,@Param("code")String code);

    @Insert("insert into offer_movie(offer_id,movie_id) values(#{offerId},#{movieId})")
    int insertOfferMovie(@Param("offerId")int offerId,@Param("movieId")int movieId);

    @Select("select id from offer_detail where offer_id = #{offerId} and code = #{code}")
    int checkCodeOffer(@Param("offerId")int offerId,@Param("code")String code);
}
