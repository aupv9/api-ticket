package com.apps.domain.repository;

import com.apps.domain.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;


@Component
public class RoomCustomRepository implements Repository<Room>{

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
            stmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            Room room = (Room) object;
            stmt.setString(1,room.getCode());
            stmt.setString(2,room.getName());
            stmt.setInt(3,room.getTheaterId());
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
