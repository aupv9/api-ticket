package com.apps.domain.repository;

import java.sql.SQLException;

public interface Repository<T> {
    <T> int  insert(T object,String sql) throws SQLException;

}
