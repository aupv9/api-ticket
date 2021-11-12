package com.apps.service.impl;

import com.apps.contants.Utilities;
import com.apps.domain.entity.Offer;
import com.apps.domain.repository.OfferCustomRepository;
import com.apps.mybatis.mysql.PromotionRepository;
import com.apps.request.OfferDto;
import com.apps.service.PromotionService;
import com.apps.service.UserService;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import xyz.downgoon.snowflake.Snowflake;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class PromotionServiceImp implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final UserService userService;
    private final OfferCustomRepository offerCustomRepository;
    public PromotionServiceImp(PromotionRepository promotionRepository, UserService userService, OfferCustomRepository offerCustomRepository) {
        this.promotionRepository = promotionRepository;
        this.userService = userService;
        this.offerCustomRepository = offerCustomRepository;
    }

    @Override
    public List<Offer> findAll(int limit, int offset, String sort, String order, String startDate, String endDate, Integer creationBy,
                               String creationDate, boolean anonProfile,
                               String promotionType, String method, boolean multi,String search) {
        return this.promotionRepository.findAll(limit,offset,sort,order,startDate,endDate,creationBy > 0 ? creationBy : null,creationDate,
                anonProfile,promotionType,method,multi,search);
    }

    @Override
    public int findAllCount(String startDate, String endDate, Integer creationBy,
                            String creationDate, boolean anonProfile, String promotionType,
                            String method, boolean multi,String search) {
        return this.promotionRepository.findAllCount(startDate,endDate,creationBy > 0 ? creationBy : null,creationDate,
                anonProfile,promotionType,method,multi,search);
    }

//            stmt.setString(1, offer.getName());
//            stmt.setString(2, offer.getCreationDate());
//            stmt.setString(3, offer.getStartDate());
//            stmt.setString(4, offer.getEndDate());
//            stmt.setString(5, offer.getType());
//            stmt.setString(6, offer.getMethod());
//            stmt.setInt(7,offer.getCreationBy());
//            stmt.setDouble(8,offer.getMaxDiscount());
//            stmt.setDouble(9,offer.getMaxTotalUsage());
//            stmt.setInt(10,offer.getMaxUsagePerUser());
//            stmt.setString(11,offer.getRule());
//            stmt.setDouble(12,offer.getPercentage());
//            stmt.setBoolean(13,offer.isAnonProfile());
//            stmt.setBoolean(14,offer.isAllowMultiple());
//            stmt.setString(15,offer.getMessage());
    @Override
    public int insertOffer(OfferDto offerDto) throws SQLException {
        String sql = "insert into offer(name,creation_date,start_date,end_date,type,method,creationBy," +
                "max_discount,max_total_usage,max_usage_per_user,rule,percentage,anon_profile," +
                "allow_multiple,message) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?)";

        var offer = Offer.builder()
                .name(offerDto.getName()).type(offerDto.getType()).method(offerDto.getMethod())
                .startDate(convertISOtoLocalDatetime(offerDto.getStartDate())).endDate(
                        convertISOtoLocalDatetime(offerDto.getEndDate()))
                .creationBy(offerDto.getCreationBy()).creationDate(Utilities.getCurrentTime())
                .maxDiscount(offerDto.getMaxDiscount()).maxTotalUsage(offerDto.getMaxTotalUsage())
                .maxUsagePerUser(offerDto.getMaxUsagePerUser()).rule(offerDto.getRule())
                .percentage(offerDto.getPercentage()).anonProfile(offerDto.isAnonProfile())
                .allowMultiple(offerDto.isAllowMultiple()).message(offerDto.getMessage())
                .build();

        int idOffer = this.offerCustomRepository.insert(offer,sql);
        if(idOffer > 0){
            switch (offer.getMethod()){
                case "Coupon":
                    this.insertOfferDetail(idOffer,offerDto.getCode());
                    this.insertOfferMovie(idOffer,offerDto.getMovieIds());
                    break;
                case "Voucher":
                    this.insertOfferMovie(idOffer,offerDto.getMovieIds());
                    this.insertCodeChoVoucher(idOffer,offerDto.getCountCode());
                    break;
            }
        }
        return idOffer;
    }

    private String convertISOtoLocalDatetime(String isoDate){
        return Timestamp.from(Instant.parse(isoDate)).toString();
    }

    private int insertOfferDetail(int offerId,String code){
        return this.promotionRepository.insertOfferDetail(offerId,code);
    }

    private void insertOfferMovie(int offerId,Integer[] movieIds){
        for (Integer movie:movieIds){
            this.promotionRepository.insertOfferMovie(offerId,movie);
        }
    }

    private void insertCodeChoVoucher(int offerId,int count){
//        String code = RandomStringUtils.random(7, true, true);
//        while (count > 0 && !this.checkCodeOfOffer(offerId,code)){
//            this.insertOfferDetail(offerId,code);
//            count--;
//        }
        Snowflake snowflake = new Snowflake(1, 1);
        for (int i = 0; i < count; i++) {
            long promoCode = snowflake.nextId();
            this.insertOfferDetail(offerId,String.valueOf(promoCode));
        }
    }

    private boolean checkCodeOfOffer(int offerId,String code){
        return this.promotionRepository.checkCodeOffer(offerId,code) > 0;
    }

}
