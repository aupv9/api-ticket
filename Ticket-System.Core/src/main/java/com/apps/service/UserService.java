package com.apps.service;

import com.apps.domain.entity.User;
import com.apps.domain.entity.UserInfo;
import com.apps.mapper.UserDto;
import com.apps.mapper.UserRegisterDto;
import com.apps.request.GoogleLoginRequest;
import com.apps.response.UserLoginResponse;
import com.apps.response.entity.UserSocial;
import com.nimbusds.jose.JOSEException;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException;
    List<User> findAllUser(int limit, int offset, String sort, String order, String name, Integer role);
    List<UserSocial> findAllUserSocial(int limit, int offset, String sort, String order, String name, Integer role);


    int findCountAll(String name,Integer role);
    int findCountAllSocial(String name,Integer role);

    User findById(int id);
    int update(UserDto userDto);
    UserLoginResponse authenticate(String email, String password) throws JOSEException;
    UserLoginResponse authenticateWithGoogle(GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException;

}
