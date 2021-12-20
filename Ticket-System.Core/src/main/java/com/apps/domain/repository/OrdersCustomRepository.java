package com.apps.domain.repository;

import com.apps.domain.entity.Category;
import com.apps.domain.entity.Orders;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class OrdersCustomRepository implements Repository<Orders>{

    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    public OrdersCustomRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof Orders) ) return 0;
            Orders orders = (Orders) object;
            stmt.setInt(1,orders.getUserId());
            stmt.setInt(2,orders.getShowTimesDetailId());
            stmt.setString(3,orders.getCreatedDate());
            stmt.setString(4,orders.getNote());
            stmt.setInt(5,orders.getCreation());
            stmt.setBoolean(6,orders.isProfile());
            stmt.setString(7,orders.getStatus());
            stmt.setString(8,orders.getExpirePayment());
            stmt.setDouble(9,orders.getTotal());
            stmt.setString(10,orders.getCode());
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
