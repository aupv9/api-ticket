package com.apps.domain.repository;

import com.apps.domain.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MemberCustomRepository implements Repository<Member> {
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
            if(!(object instanceof Member) ) return 0;
            Member member = (Member) object;
            stmt.setString(1, member.getNumber());
            stmt.setString(2,member.getPin());
            stmt.setInt(3,member.getUserId());
            stmt.setString(4,member.getCreationDate());
            stmt.setString(5,member.getStartDate());
            stmt.setString(6,member.getEndDate());
            stmt.setString(7,member.getLevel());
            stmt.setDouble(8,member.getPoint());
            stmt.setBoolean(9,member.getProfile());
            stmt.setString(10,member.getCmnd());
            stmt.setString(11,member.getBirthday());

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
