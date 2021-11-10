package com.apps.service;

import com.apps.domain.entity.Promotion;

import java.util.List;

public interface PromotionService {

    List<Promotion> findAll(int limit, int offset, String sort, String order,
                            String createdDate, String startDate,
                            String endDate, String displayName, String creationDate,
                            boolean enable,String beginUsable,String endUsable,boolean global,
                            boolean anonProfile,String promotionType);
    int findAllCount(String createdDate, String startDate,
                     String endDate, String displayName, String creationDate,
                     boolean enable,String beginUsable,String endUsable,boolean global,
                     boolean anonProfile,String promotionType);
}
