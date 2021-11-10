package com.apps.service.impl;

import com.apps.domain.entity.Payment;
import com.apps.domain.entity.Promotion;
import com.apps.mybatis.mysql.PromotionRepository;
import com.apps.service.PromotionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionServiceImp implements PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionServiceImp(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }


    @Override
    public List<Promotion> findAll(int limit, int offset, String sort, String order, String createdDate, String startDate, String endDate, String displayName, String creationDate, boolean enable, String beginUsable, String endUsable, boolean global, boolean anonProfile, String promotionType) {
        return null;

    }

    @Override
    public int findAllCount(String createdDate, String startDate, String endDate, String displayName, String creationDate, boolean enable, String beginUsable, String endUsable, boolean global, boolean anonProfile, String promotionType) {
        return 0;
    }
}
