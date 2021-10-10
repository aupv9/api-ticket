package com.apps.domain.repository;

import com.apps.domain.entity.ShowTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class ShowTimesCustomRepository implements Repository<ShowTimes>{

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
            if(!(object instanceof ShowTimes) ) return 0;
            ShowTimes showTimes = (ShowTimes) object;
            stmt.setString(1, showTimes.getStartDate());
            stmt.setString(2,showTimes.getEndDate());
            stmt.setString(3,showTimes.getCreationDate());
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
