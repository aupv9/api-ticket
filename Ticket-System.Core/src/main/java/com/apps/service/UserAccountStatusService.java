package com.apps.service;

import com.apps.domain.entity.UserAccountStatus;
import com.apps.response.ResponseStatus;

import java.util.List;

public interface UserAccountStatusService {
    UserAccountStatus findById(int id);
    ResponseStatus insert(UserAccountStatus accountStatus) ;
    ResponseStatus update(UserAccountStatus accountStatus) ;
    ResponseStatus delete(int id);
    List<UserAccountStatus> findAll();
    int findAllCount();
}
