package com.apps.service;

import com.apps.domain.entity.OfferHistory;

import java.util.List;

public interface OfferHistoryService {
    List<OfferHistory> findAll(int limit,int offset,
                              String sort, String order,int userId,
                               int offerId, String status,
                              String timeUsed,int orderId,String search);
    int findAllCount(int userId, int offerId,String status,
                     String timeUsed,int orderId,String search);
    OfferHistory findByOrder(int orderId);
    OfferHistory findById(int id);

}
