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
            Orders category = (Orders) object;
            stmt.setInt(1,category.getUserId());
            stmt.setInt(2,category.getShowTimesDetailId());
            stmt.setDouble(3,category.getTax());
            stmt.setString(4,category.getCreateDate());
            stmt.setString(5,category.getNote());
            stmt.setInt(6,category.getCreation());
            stmt.setString(7,category.getTypeUser());
            stmt.setString(8,category.getStatus());
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
