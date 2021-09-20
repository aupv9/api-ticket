package com.apps.domain.repository;

import com.apps.domain.entity.ShowTimesDetail;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class ShowTimesDetailCustomRepository implements com.apps.domain.repository.Repository<ShowTimesDetail> {


    @Override
    public <T> int insert(T object, String sql) throws SQLException {
        return 0;
    }
}
