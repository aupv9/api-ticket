package com.apps.service;

import com.apps.domain.entity.UserAccountStatus;
import com.apps.response.ResponseStatus;
import org.springframework.data.domain.Page;

public interface UserAccountStatusService {
    Page<com.apps.jpa.entity.UserAccountStatus> findAll(int page, int size);
    UserAccountStatus findById(int id);
    ResponseStatus insert(UserAccountStatus accountStatus) ;
    ResponseStatus update(UserAccountStatus accountStatus) ;
    ResponseStatus delete(int id);
}
