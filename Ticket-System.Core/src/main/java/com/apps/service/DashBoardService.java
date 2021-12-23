package com.apps.service;

import com.apps.response.entity.PercentCoverRoom;
import com.apps.response.entity.RevenueEmployee;

import java.util.List;

public interface DashBoardService {
    List<RevenueEmployee> findAllRevenue(Integer limit, Integer offset,
                                         String sort, String order,
                                         Integer roleId, Integer theaterId,String date);
    int findCountAll(Integer roleId,Integer theaterId);

    List<PercentCoverRoom> getPercentCoverSeatOnTheater(String date,Integer theater);


}

