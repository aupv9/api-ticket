package com.apps.domain.repository;

import com.apps.domain.entity.Category;
import com.apps.domain.entity.ShowTimesDetail;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class CategoryCustomRepository implements Repository<Category> {

    private final DataSource dataSource;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    public CategoryCustomRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insert(Object object, String sql) throws SQLException {
        Connection connection = null;
        int generatedKey = 0 ;
        try{
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(!(object instanceof Category) ) return 0;
            Category category = (Category) object;
            stmt.setString(1,category.getName());
            stmt.setString(2,category.getDescription());
            stmt.setString(3,category.getType());
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
