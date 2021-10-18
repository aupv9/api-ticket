package com.apps.domain.repository;

import com.apps.domain.entity.UserInfo;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class UserCustomRepository implements Repository<UserInfo>{

    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    public UserCustomRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof UserInfo)) return 0;
            UserInfo userInfo = (UserInfo) object;
            stmt.setString(1,userInfo.getEmail());
            stmt.setString(2,userInfo.getFirstName());
            stmt.setString(3,userInfo.getLastName());
            stmt.setString(4,userInfo.getFullName());
            stmt.setBoolean(5,userInfo.getIsLoginSocial());
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
