package com.apps.domain.repository;

import com.apps.domain.entity.Location;
import com.apps.domain.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class PaymentCustomRepository implements Repository<Payment> {

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
            if(!(object instanceof Payment) ) return 0;
            Payment payment = (Payment) object;
            stmt.setInt(1,payment.getPartId());
            stmt.setInt(2,payment.getPaymentMethodId());
            stmt.setInt(3,payment.getCreation());
            stmt.setString(4,payment.getStatus());
            stmt.setString(5,payment.getTransactionId());
            stmt.setString(6,payment.getCreatedDate());
            stmt.setDouble(7,payment.getAmount());
            stmt.setString(8,payment.getUseFor());
            stmt.setString(9,payment.getNote());
            stmt.setInt(10,payment.getUserId());
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
