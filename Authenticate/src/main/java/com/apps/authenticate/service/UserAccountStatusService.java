package com.apps.authenticate.service;

import com.apps.authenticate.entity.UserAccountStatus;
import com.apps.authenticate.exception.ApplicationException;

import java.util.List;

public interface UserAccountStatusService {
    List<UserAccountStatus> findAll();
    UserAccountStatus findById(int id) throws ApplicationException;
    int insert(UserAccountStatus accountStatus);
    int update(UserAccountStatus accountStatus) throws ApplicationException;
    int delete(int id) throws ApplicationException;
}
