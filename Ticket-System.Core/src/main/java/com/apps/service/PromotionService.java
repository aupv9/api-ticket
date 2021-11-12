package com.apps.service;

import com.apps.domain.entity.Offer;
import com.apps.request.OfferDto;

import java.sql.SQLException;
import java.util.List;

public interface PromotionService {

    List<Offer> findAll( int limit,int offset,String sort, String order,
                        String startDate,String endDate,Integer creationBy,
                        String creationDate,boolean anonProfile,
                        String promotionType, String method,boolean multi,String search);
    int findAllCount( String startDate,String endDate,Integer creationBy,
                      String creationDate,boolean anonProfile,
                      String promotionType, String method,boolean multi ,String search);
    int insertOffer(OfferDto offerDto) throws SQLException;

}
