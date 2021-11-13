package com.apps.service.impl;

import com.apps.domain.entity.OfferHistory;
import com.apps.mybatis.mysql.OfferHistoryRepository;
import com.apps.service.OfferHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OfferHistoryServiceImpl implements OfferHistoryService {

    private final OfferHistoryRepository offerHistoryRepository;

    public OfferHistoryServiceImpl(OfferHistoryRepository offerHistoryRepository) {
        this.offerHistoryRepository = offerHistoryRepository;
    }

    @Override
    public List<OfferHistory> findAll(int limit, int offset, String sort, String order, int userId, int offerId, String status, String timeUsed) {
        return this.offerHistoryRepository.findAll(limit,offset,sort,order,userId > 0 ?userId : null,
                offerId > 0 ? offerId :  null,status,timeUsed);
    }

    @Override
    public int findAllCount(int userId, int offerId, String status, String timeUsed) {
        return this.offerHistoryRepository.findAllCount(userId > 0 ?userId : null,
                offerId > 0 ? offerId :  null,status,timeUsed);
    }
}
