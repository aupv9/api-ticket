package com.apps.domain.repository;

import com.apps.domain.entity.Concession;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class ConcessionsCustomRepository implements Repository<Concession> {
    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    public ConcessionsCustomRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof Concession) ) return 0;
            Concession concessions = (Concession) object;
            stmt.setString(1, concessions.getName());
            stmt.setDouble(2, concessions.getPrice());
            stmt.setInt(3, concessions.getCategoryId());
            stmt.setString(4, concessions.getImage());
            stmt.setString(5, concessions.getThumbnail());

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
