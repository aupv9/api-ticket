package com.apps.service;

import com.apps.domain.entity.Offer;
import com.apps.domain.entity.OfferCode;
import com.apps.domain.entity.OfferDetail;
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

    OfferCode checkPromotionCode(String code,Integer movie);

    Offer findById(int id);

    List<OfferDetail> findAllOfferDetail(Integer limit, Integer offset, String sort, String order,
                              Integer offer);
    int findAllCountOfferDetail(Integer offer);

    int insertSubNewsLetter(String email);

    int sendToSubscriber(List<Integer> offerId);

    List<String> findAllSubscriber();


}
