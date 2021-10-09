package com.apps.domain.repository;

import com.apps.domain.entity.Location;
import com.apps.domain.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MovieCustomRepository implements Repository<Movie>{
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
            if(!(object instanceof Movie) ) return 0;
            Movie room = (Movie) object;
            stmt.setString(1, room.getName());
            stmt.setString(2,room.getThumbnail());
            stmt.setString(3,room.getImage());
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
