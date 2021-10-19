package com.apps.service;

import com.apps.domain.entity.User;
import com.apps.domain.entity.UserAccount;
import com.apps.mapper.UserRegisterDto;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException;
    int activeUserAccount(UserAccount userAccount);
     List<User> findAllUser(int limit, int offset, String sort, String order, String name, Integer role);
    int findCountAll(String name,Integer role);

}
