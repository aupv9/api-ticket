package com.apps.service;

import com.apps.domain.entity.Media;

import java.sql.SQLException;

public interface MediaService {
    int insert(Media media) throws SQLException;
    Media findById(Integer id);
    Media findByPath(String path);

}
