package com.apps.domain.repository;

import com.apps.domain.entity.UserAccount;
import com.apps.domain.entity.UserInfo;

import java.sql.SQLException;

public interface IUserCustomHandler {
    int insertUserInfo(UserInfo userInfo,String sql) throws SQLException;
    int insertUserAccount(UserAccount userAccount,String sql) throws SQLException;
}
