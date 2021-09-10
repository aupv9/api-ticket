package com.apps.authenticate.response;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Builder
public class ResponseStatus {
    private StatusType status;
    private StatusMessage message;
    private List<Object> results;
    private Object result;

    public ResponseStatus() {
    }

    public ResponseStatus(StatusType status, StatusMessage message, Object... results) {
        this.status = status;
        this.message = message;
        this.results = Arrays.asList(results);
    }


    public ResponseStatus(StatusMessage message, Object[] result) {
        this(StatusType.SUCCESS,message,result);
    }

    public enum StatusType {
        SUCCESS,FAIL,WARNING,INFO,ERROR
    }
    @AllArgsConstructor
    public enum StatusMessage{
        NOT_FOUND_ACCOUNT_STATUS("Not Found "),
        USER_ACCOUNT_IS_NULL("User Account Is Null"),
        USER_ACCOUNT_LIST_IS_NULL("User Account List Is Null"),
        ID_IS_NULL("Id Is Null"),
        USER_ACCOUNT_CREATE_SUCCESS("User Account CREATED");

        private String message;

    }

}
