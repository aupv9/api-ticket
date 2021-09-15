package com.apps.service;


import com.apps.domain.entity.Tier;

import java.sql.SQLException;
import java.util.List;

public interface TierService {
    List<Tier> findAll(Integer page, Integer size);
    int insert(Tier Tier) throws SQLException;
}
