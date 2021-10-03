package com.apps.domain.repository;

import com.apps.domain.entity.Seat;
import com.apps.domain.entity.Tier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;


@Component
public class SeatCustomRepository implements Repository<Seat>{


    @Autowired
    private DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Seat seat = (Seat) object;
//            stmt.setFloat(1, seat.getPrice());
//            stmt.setInt(2,seat.getSeatTypeId());
//            stmt.setInt(3, seat.get());
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            while (rs.next()){
                generatedKey = rs.getInt(1);
            }
        }finally {
            assert connection != null;
            if(!connection.isClosed()) connection.close();
            if(!rs.isClosed()) rs.close();
            if(!stmt.isClosed()) stmt.close();
        }
        return generatedKey;
    }
}
