package com.apps.service;

import com.apps.domain.entity.User;
import com.apps.domain.entity.UserInfo;
import com.apps.mapper.UserDto;
import com.apps.mapper.UserRegisterDto;
import com.apps.request.GoogleLoginRequest;
import com.apps.response.UserLoginResponse;
import com.apps.response.entity.UserSocial;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException;
    List<com.apps.response.entity.UserDto> findAllUser(int limit, int offset, String sort, String order, String name, Integer role);
    List<UserSocial> findAllUserSocial(int limit, int offset, String sort, String order, String name, Integer role);


    int findCountAll(String name,Integer role);
    int findCountAllSocial(String name,Integer role);

    com.apps.response.entity.UserDto findById(int id);
    int update(UserDto userDto);
    UserLoginResponse authenticate(String email, String password) throws JOSEException;
    UserLoginResponse authenticateWithGoogle(GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException;
    String getNowDateTime();
    boolean checkEmailAlready(String email);
    int updateCurrentLogged(int userId,boolean isLogged);
    int updateCurrentLoggedByEmail(String email);
    int getTheaterByUser();
    boolean isSeniorManager(Integer userId);
    boolean isManager(Integer userId);
    int getUserFromContext();
}
