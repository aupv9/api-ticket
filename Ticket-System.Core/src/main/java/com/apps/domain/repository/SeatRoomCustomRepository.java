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

//    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public <T> int insert(T object, String sql) throws SQLException {
        return 0;
    }

    public void clockSeatById(int id) throws SQLException {
        Connection connection = null;
        try{

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            stmt.addBatch("start transaction");
            stmt.execute();
            stmt = connection.prepareStatement("SELECT status FROM seat_room WHERE id = ? FOR UPDATE;");
            stmt.setInt(1,id);
            boolean result  = stmt.execute();
            log.info("Result clock seat room : "+ result);
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
