package com.apps.domain.repository;

import com.apps.domain.entity.Concession;
import com.apps.domain.entity.Offer;
import com.apps.request.OfferDto;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class OfferCustomRepository implements Repository<Offer> {
    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    public OfferCustomRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof Offer) ) return 0;
            Offer offer = (Offer) object;
            stmt.setString(1, offer.getName());
            stmt.setString(2, offer.getCreationDate());
            stmt.setString(3, offer.getStartDate());
            stmt.setString(4, offer.getEndDate());
            stmt.setString(5, offer.getType());
            stmt.setString(6, offer.getMethod());
            stmt.setInt(7,offer.getCreationBy());
            stmt.setDouble(8,offer.getMaxDiscount());
            stmt.setDouble(9,offer.getMaxTotalUsage());
            stmt.setInt(10,offer.getMaxUsagePerUser());
            stmt.setString(11,offer.getRule());
            stmt.setDouble(12,offer.getPercentage());
            stmt.setBoolean(13,offer.isAnonProfile());
            stmt.setBoolean(14,offer.isAllowMultiple());
            stmt.setString(15,offer.getMessage());
            stmt.setString(16,offer.getStatus());
            stmt.setDouble(17,offer.getDiscountAmount());

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
