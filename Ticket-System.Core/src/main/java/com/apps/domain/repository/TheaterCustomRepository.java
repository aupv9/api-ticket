package com.apps.domain.repository;

import com.apps.domain.entity.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class TheaterCustomRepository implements Repository<Theater>{


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
            Theater theater = (Theater) object;
            stmt.setString(1, theater.getName());
            stmt.setString(2,theater.getAddress());
            stmt.setString(3, theater.getLatitude());
            stmt.setString(4, theater.getLongitude());
            stmt.setString(5, theater.getThumbnail());
            stmt.setString(6, theater.getImage());
            stmt.setInt(7, theater.getLocationId());
            stmt.setBoolean(8, theater.isActive());

            stmt.execute();
            rs = stmt.getGeneratedKeys();
            while (rs.next()){
                generatedKey = rs.getInt(1);
            }
        }finally {
            assert connection != null;
            if(!connection.isClosed()) connection.close();
            if(!stmt.isClosed()) stmt.close();
        }
        return generatedKey;
    }
}
