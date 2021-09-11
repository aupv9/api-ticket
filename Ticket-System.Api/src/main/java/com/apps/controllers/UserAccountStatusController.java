package com.apps.controllers;

import com.apps.domain.entity.UserAccountStatus;
import com.apps.response.ResponseStatus;
import com.apps.service.UserAccountStatusService;
import com.apps.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/authenticate")
@Slf4j
public class UserAccountStatusController {

    @Autowired
    private UserAccountStatusService accountStatusService;

    @GetMapping(value = "/account-status",produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAll(@RequestParam(value = "page") Integer page,
                                     @RequestParam(value = "size") Integer size){
        Page<com.apps.jpa.entity.UserAccountStatus> accountStatuses = accountStatusService.findAll(page,size);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus(ResponseStatus.StatusType.SUCCESS);
        responseStatus.setResult(accountStatuses);
        return ResponseEntity.ok(responseStatus);
    }

    @GetMapping(value = "/account-status-id",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> findById(@RequestParam(value = "id",required = false) Integer id) {
        ResponseStatus status = new ResponseStatus();
        if(id == null){
            status.setStatus(ResponseStatus.StatusType.WARNING);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
        }else{
            UserAccountStatus accountStatus = accountStatusService.findById(id);
            if(accountStatus == null){
                status.setStatus(ResponseStatus.StatusType.FAIL);
                status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
            }else{
                status.setStatus(ResponseStatus.StatusType.SUCCESS);
                status.setResult(accountStatus);
            }
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping(value = "/account-status",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody UserAccountStatus userAccountStatus) {
        ResponseStatus status = new ResponseStatus();
        if(userAccountStatus == null){
            status.setStatus(ResponseStatus.StatusType.WARNING);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
            return ResponseEntity.ok(status);
        }
        status = accountStatusService.insert(userAccountStatus);
        return ResponseEntity.ok(status);
    }

    @PutMapping(value = "/account-status",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateAccountStatus(@RequestBody UserAccountStatus userAccountStatus) {
        ResponseStatus status = new ResponseStatus();
        if(userAccountStatus == null){
            status.setStatus(ResponseStatus.StatusType.WARNING);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
            return ResponseEntity.ok(status);
        }else{
            UserAccountStatus accountStatus = this.accountStatusService.findById(userAccountStatus.getId());
            if(CommonUtils.isNullOrEmpty(accountStatus)){
                status.setStatus(ResponseStatus.StatusType.ERROR);
                status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
                return ResponseEntity.ok(status);
            }else{
                UserAccountStatus accountBuild = UserAccountStatus.builder().
                        id(userAccountStatus.getId()).
                        code(userAccountStatus.getCode()).
                        name(userAccountStatus.getName()).build();
                status = this.accountStatusService.update(accountBuild);
            }
        }
        return ResponseEntity.ok(status);
    }

    @DeleteMapping(value = "/account-status",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> deleteById(@RequestParam(value = "id") Integer id) {
        ResponseStatus status = new ResponseStatus();
        if(id == null){
            status.setStatus(ResponseStatus.StatusType.WARNING);
            status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
            return ResponseEntity.ok(status);
        }else{
            UserAccountStatus accountStatus = this.accountStatusService.findById(id);
            if(CommonUtils.isNullOrEmpty(accountStatus)){
                status.setStatus(ResponseStatus.StatusType.ERROR);
                status.setMessage(ResponseStatus.StatusMessage.USER_ACCOUNT_IS_NULL);
                return ResponseEntity.ok(status);
            }else{
                status = accountStatusService.delete(id);
            }
        }
        return ResponseEntity.ok(status);
    }



}
