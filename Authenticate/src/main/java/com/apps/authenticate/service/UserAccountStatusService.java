package com.apps.authenticate.service;

import com.apps.authenticate.entity.UserAccountStatus;
import com.apps.authenticate.response.ResponseStatus;
import org.springframework.data.domain.Page;


public interface UserAccountStatusService {
    Page<com.apps.authenticate.jpa.entity.UserAccountStatus> findAll(int page, int size);
    UserAccountStatus findById(int id);
    ResponseStatus insert(UserAccountStatus accountStatus) ;
    ResponseStatus update(UserAccountStatus accountStatus) ;
    ResponseStatus delete(int id);
}
