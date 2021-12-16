package com.apps.service;

import com.apps.domain.entity.Tag;

import java.sql.SQLException;

public interface TagService {
    int insert(Tag tag) throws SQLException;
    Tag findByName(String name);
}
