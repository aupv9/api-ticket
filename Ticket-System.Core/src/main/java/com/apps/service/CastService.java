package com.apps.service;

import com.apps.domain.entity.Cast;

import java.sql.SQLException;

public interface CastService {
    int insert(Cast cast) throws SQLException;

    Cast findByName(String name);

    Cast findById(Integer id);
}
