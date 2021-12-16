package com.apps.service.impl;

import com.apps.domain.entity.Cast;
import com.apps.domain.repository.CastCustomRepository;
import com.apps.mybatis.mysql.CastRepository;
import com.apps.service.CastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class CastServiceImpl implements CastService {

    private final CastRepository castRepository;
    private final CastCustomRepository castCustomRepository;

    @Override
    public int insert(Cast cast) throws SQLException {
        String sql = "insert into cast (name, profile, role, id) VALUES (?, ?, ?, ?)";
        return this.castCustomRepository.insert(cast,sql);
    }

    @Override
    public Cast findByName(String name) {
        return this.castRepository.findByName(name);
    }

    @Override
    public Cast findById(Integer id) {
        return this.castRepository.findById(id);
    }
}
