package com.apps.service.impl;

import com.apps.domain.entity.Tag;
import com.apps.domain.repository.TagCustomRepository;
import com.apps.mybatis.mysql.TagRepository;
import com.apps.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagCustomRepository tagCustomRepository;

    @Override
    public int insert(Tag tag) throws SQLException {
        return this.tagCustomRepository.insert(tag
        ,"insert into movie_type(name) values(?)");
    }

    @Override
    public Tag findByName(String name) {
        return this.tagRepository.findByName(name);
    }
}
