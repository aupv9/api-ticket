package com.apps.domain.repository;

import com.apps.domain.entity.Seat;
import com.apps.domain.entity.SeatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@Slf4j
public class SeatRoomCustomRepository implements Repository<SeatRoom>{

    @Autowired
    private DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public <T> int insert(T object, String sql) throws SQLException {
        return 0;
    }

    public int clockSeatById(int id, String status) throws SQLException {
        Connection connection = null;
        CallableStatement statement = null;
        boolean hadResults = false;

        try{
            connection = dataSource.getConnection();
            statement = connection.prepareCall("{call clockSeatRoom(?,?,?)}");
            statement.setInt(1,id);
            statement.setString(2,status);
            statement.registerOutParameter(3,Types.INTEGER);
            hadResults = statement.execute();
            return statement.getInt(3);
        }finally {
            assert connection != null;
            if(!connection.isClosed()) connection.close();

        }
    }

    public void unClockSeatById() throws SQLException {
        Connection connection = null;
        try{
            connection = stmt.getConnection();
            stmt = connection.prepareStatement("start transaction");
            stmt.execute();
        }finally {
            assert connection != null;
            if(!connection.isClosed()) connection.close();
            if(!rs.isClosed()) rs.close();
            if(!stmt.isClosed()) stmt.close();
        }
    }
}
