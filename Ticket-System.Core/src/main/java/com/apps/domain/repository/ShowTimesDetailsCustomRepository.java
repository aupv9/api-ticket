package com.apps.domain.repository;

import com.apps.domain.entity.ShowTimesDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class ShowTimesDetailsCustomRepository implements Repository<ShowTimesDetail>{

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
            if(!(object instanceof ShowTimesDetail) ) return 0;
            ShowTimesDetail showTimes = (ShowTimesDetail) object;
            stmt.setInt(1,showTimes.getMovieId());
            stmt.setInt(2,showTimes.getRoomId());
            stmt.setString(3,showTimes.getTimeStart());
            stmt.setString(4,showTimes.getTimeEnd());
            stmt.setDouble(5,showTimes.getPrice());

            stmt.execute();
            rs = stmt.getGeneratedKeys();
            while (rs.next()){
                generatedKey = rs.getInt(1);
            }
            connection.commit();

        }finally {
            assert connection != null;
            if(rs != null) rs.close();
            if(!connection.isClosed()) connection.close();
            if(!stmt.isClosed()) stmt.close();
        }
        return generatedKey;
    }
}
