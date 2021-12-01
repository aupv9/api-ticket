package com.apps.domain.repository;

import com.apps.domain.entity.Privilege;
import com.apps.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@RequiredArgsConstructor
public class RoleCustomRepository implements Repository<Role>{


    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof Role) ) return 0;
            Role role = (Role) object;
            stmt.setString(1,role.getName());
            stmt.setString(2,role.getCode());
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
