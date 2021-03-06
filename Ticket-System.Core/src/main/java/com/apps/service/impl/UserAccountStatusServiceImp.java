package com.apps.service.impl;

import com.apps.domain.entity.UserAccountStatus;
import com.apps.mybatis.mysql.UserAccountStatusRepository;
import com.apps.response.ResponseStatus;
import com.apps.service.UserAccountStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UserAccountStatusServiceImp implements UserAccountStatusService {

    @Autowired
    private UserAccountStatusRepository statusRepository;


    @Cacheable(cacheNames = "userAccountStatus",key = "'UserAccountStatusRepository.findById_'+#id", unless = "#result == null")
    @Override
    public UserAccountStatus findById(int id)  {
        return statusRepository.findById(id);
    }

    @Override
    public ResponseStatus insert(UserAccountStatus accountStatus)  {
        ResponseStatus status = new ResponseStatus();
        int result = statusRepository.insert(accountStatus);
        if(result > 0){
            status.setStatus(ResponseStatus.StatusType.SUCCESS);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_CREATE_SUCCESS);
            status.setResult(result);
        }else{
            status.setStatus(ResponseStatus.StatusType.FAIL);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_CREATE_SUCCESS);
            status.setResult(result);
        }
        return status ;
    }

    @Override
    public ResponseStatus update(UserAccountStatus accountStatus) {
        ResponseStatus status = new ResponseStatus();
        UserAccountStatus userAccountStatus = UserAccountStatus.builder()
                                                .id(accountStatus.getId())
                                                .code(accountStatus.getCode())
                                                .name(accountStatus.getName()).build();
        int result = this.statusRepository.update(userAccountStatus);
        if(result > 0){
            status.setStatus(ResponseStatus.StatusType.SUCCESS);
            status.setResult(accountStatus);
        }else{
            status.setStatus(ResponseStatus.StatusType.FAIL);
        }
        return status;
    }

    @Override
    public ResponseStatus delete(int id)  {
        log.info("UserAccountStatus : "+ id);
        ResponseStatus status = new ResponseStatus();

        int result = this.statusRepository.deleteById(id);
        log.info("Result delete : "+ result);

        if(result > 0){
            status.setStatus(ResponseStatus.StatusType.SUCCESS);
        }else{
            status.setStatus(ResponseStatus.StatusType.FAIL);
        }
        return status;
    }

    @Override
    public List<UserAccountStatus> findAll() {
        return this.statusRepository.findAll();
    }

    @Override
    public int findAllCount() {
        return this.statusRepository.findAllCount();
    }
}
