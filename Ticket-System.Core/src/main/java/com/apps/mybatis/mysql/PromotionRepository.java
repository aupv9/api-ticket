package com.apps.mybatis.mysql;

import com.apps.domain.entity.*;
import org.apache.ibatis.annotations.*;

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

    List<OfferDetail> findAllOfferDetail(@Param("limit") int limit, @Param("offset")int offset, @Param("sort") String sort, @Param("order")String order,
                                         @Param("offerId") Integer offer);

    int findAllCountOfferDetail(@Param("offerId") Integer offer);

    @Select("select max_total_usage from offer where id = #{id}")
    int countRemainUsage(@Param("id")Integer id);


    @Insert("insert into offer_detail(offer_id,code) values(#{offerId},#{code})")
    int insertOfferDetail(@Param("offerId")int offerId,@Param("code")String code);

    @Insert("insert into offer_movie(offer_id,movie_id) values(#{offerId},#{movieId})")
    int insertOfferMovie(@Param("offerId")int offerId,@Param("movieId")int movieId);

    @Select("select id from offer_detail where offer_id = #{offerId} and code = #{code}")
    int checkCodeOffer(@Param("offerId")int offerId,@Param("code")String code);

    @Select("select * from offer_detail where code = #{code}")
    OfferCode checkPromotionCode(@Param("code")String code);

    @Select("select * from offer_movie where offer_id =#{offer} and movie_id = #{movie}")
    OfferMovie findOfferMovie(@Param("offer")Integer offer,@Param("movie")Integer movie);

    @Select("select * from offer where id = #{id}")
    Offer findById(@Param("id") int id);


    int insertOfferHistory(@Param("offer")OfferHistory offerHistory);

    @Update("update offer set max_total_usage = #{count} where id = #{id}")
    int updateMaxTotalUsage(@Param("count")Integer remain,@Param("id")Integer id);
}
