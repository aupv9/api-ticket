package com.apps.service;

import com.apps.response.entity.*;

import java.util.List;

public interface DashBoardService {
    List<RevenueEmployee> findAllRevenue(Integer limit, Integer offset,
                                         String sort, String order,
                                         Integer roleId, Integer theaterId,String date);
    int findCountAll(Integer roleId,Integer theaterId);

    List<PercentCoverRoom> getPercentCoverSeatOnTheater(String date,Integer theater);

    List<PercentPaymentMethod> getPercentPaymentMethod(String date);

    List<ConcessionRevenue> getRevenueConcession(String startDate,String endDate,Integer creation);

    List<RevenueMethod> getRevenueMethod(String startDate,String endDate,Integer creation);


}

