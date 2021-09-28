package com.apps.service.impl;

import com.apps.domain.entity.Tier;
import com.apps.domain.repository.TierCustomRepository;
import com.apps.mybatis.mysql.TierRepository;
import com.apps.service.TierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class TierServiceImpl implements TierService {

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private TierCustomRepository tierCustomRepository;

    @Override
    @Cacheable(value = "TierService" ,key = "'TierList_'+#page +'-'+#size" , unless = "#result == null")
    public List<Tier> findAll(Integer page, Integer size) {
        return this.tierRepository.findAll(size,page * size);
    }

    @Override
    public int insert(Tier tier) throws SQLException {
        String sql = "insert into booksystem.tier(count_seat, code, name, room_id) VALUES (?,?,?,?)";
        return this.tierCustomRepository.insert(tier,sql);
    }
}
