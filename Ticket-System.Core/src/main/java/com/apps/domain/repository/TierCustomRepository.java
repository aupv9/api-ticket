package com.apps.domain.repository;

import com.apps.domain.entity.Room;
import com.apps.domain.entity.Tier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component("TierCustomRepository")
public class TierCustomRepository implements Repository<Tier>{
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
            Tier tier = (Tier) object;
            stmt.setInt(1, tier.getCountSeat());
            stmt.setString(2,tier.getCode());
            stmt.setString(3, tier.getName());
            stmt.setInt(4, tier.getRoomBId());
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
