package com.apps.domain.repository;

import com.apps.domain.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class CastCustomRepository implements Repository<Cast>{

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
            if(!(object instanceof Cast) ) return 0;
            Cast cast = (Cast) object;
            stmt.setString(1,cast.getName());
            stmt.setString(2,cast.getProfile());
            stmt.setString(3,cast.getRole());
            stmt.setString(4,null);

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
