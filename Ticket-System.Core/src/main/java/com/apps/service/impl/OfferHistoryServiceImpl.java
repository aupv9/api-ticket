package com.apps.service.impl;

import com.apps.domain.entity.OfferHistory;
import com.apps.mybatis.mysql.OfferHistoryRepository;
import com.apps.service.OfferHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferHistoryServiceImpl implements OfferHistoryService {

    private final OfferHistoryRepository offerHistoryRepository;

    @Override
    public List<OfferHistory> findAll(int limit, int offset, String sort, String order, int userId, int offerId, String status, String timeUsed,int orderId,String search) {
        return this.offerHistoryRepository.findAll(limit,offset,sort,order,userId > 0 ? userId : null,
                offerId > 0 ? offerId : null,status,timeUsed,orderId > 0 ? orderId : null,search);
    }

    @Override
    public int findAllCount(int userId, int offerId, String status, String timeUsed,int orderId,String search) {
        return this.offerHistoryRepository.findAllCount(userId > 0 ? userId : null,
                offerId > 0 ? offerId : null,status,timeUsed,orderId > 0 ? orderId : null,search);
    }

    @Override
    public OfferHistory findByOrder(int orderId) {
        return this.offerHistoryRepository.findByOrder(orderId);
    }

    @Override
    public OfferHistory findById(int id) {
        return this.offerHistoryRepository.findById(id);
    }
}
