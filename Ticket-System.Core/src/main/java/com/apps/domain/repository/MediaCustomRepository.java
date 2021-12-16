package com.apps.domain.repository;

import com.apps.domain.entity.Cast;
import com.apps.domain.entity.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;


@Component
public class MediaCustomRepository implements Repository<Media>{

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
            if(!(object instanceof Media) ) return 0;
            Media media = (Media) object;
            stmt.setString(1,media.getCreationDate());
            stmt.setString(2,"");
            stmt.setString(3,"");
            stmt.setString(4,"");
            stmt.setString(5,"");
            stmt.setString(6,"");
            stmt.setString(7,media.getPath());

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
