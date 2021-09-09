package com.apps.authenticate.service.impl;

import com.apps.authenticate.contants.MessageData;
import com.apps.authenticate.entity.UserAccountStatus;
import com.apps.authenticate.exception.ApplicationException;
import com.apps.authenticate.repository.UserAccountStatusRepository;
import com.apps.authenticate.service.UserAccountStatusService;
import com.apps.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountStatusServiceImp implements UserAccountStatusService {

    @Autowired
    private UserAccountStatusRepository statusRepository;

    @Cacheable(cacheNames = "userAccountStatus",key = "'UserAccountStatusRepository.findAll'", unless = "#result == null")
    @Override
    public List<UserAccountStatus> findAll() {
        return statusRepository.findAll();
    }

    @Cacheable(cacheNames = "userAccountStatus",key = "'UserAccountStatusRepository.findById_'+#id", unless = "#result == null")
    @Override
    public UserAccountStatus findById(int id) throws ApplicationException {
        UserAccountStatus accountStatus = statusRepository.findById(id);
        if(accountStatus == null){
            throw new ApplicationException(MessageData.NOT_FOUND_ACCOUNT_STATUS);
        }
        return statusRepository.findById(id);
    }

    @Override
    public int insert(UserAccountStatus accountStatus) {
        if(accountStatus == null) throw new ApplicationException(Mess);
        return statusRepository.insert(accountStatus);
    }

    @Override
    public int update(UserAccountStatus accountStatus) throws ApplicationException {
        if(CommonUtils.isNullOrEmpty(String.valueOf(accountStatus.getId()))){
            UserAccountStatus userAccountStatus = this.findById(accountStatus.getId());
            if(userAccountStatus != null){
                return statusRepository.update(userAccountStatus);
            }else{
                throw new ApplicationException(MessageData.NOT_FOUND_ACCOUNT_STATUS);
            }
        }
        return -1;
    }

    @Override
    public int delete(int id) throws ApplicationException {
        if(CommonUtils.isNullOrEmpty(String.valueOf(id))){
            UserAccountStatus userAccountStatus = this.findById(id);
            if(userAccountStatus != null){
                return statusRepository.delete(id);
            }else{
                return -1;
            }
        }
        return -1;
    }
}
