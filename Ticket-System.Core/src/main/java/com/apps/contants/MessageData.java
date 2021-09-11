package com.apps.contants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public enum MessageData implements Serializable {
    NOT_FOUND_ACCOUNT_STATUS("Not Found ",HttpStatus.NOT_FOUND),
    USER_ACCOUNT_IS_NULL("User Account Is Null",HttpStatus.BAD_REQUEST),
    USER_ACCOUNT_LIST_IS_NULL("User Account List Is Null",HttpStatus.NOT_FOUND),
    ID_IS_NULL("Id Is Null",HttpStatus.BAD_REQUEST);

    private static final long serialVersionUID = 1L;
    private String pattern;
    private Level level;
    private HttpStatus httpStatus;

    MessageData(String pattern, HttpStatus httpStatus) {
        this(pattern,Level.ERROR, httpStatus);
    }

    MessageData(String pattern, Level level, HttpStatus httpStatus) {
        this.pattern = pattern;
        this.level = level;
        this.httpStatus = httpStatus;
    }

    public enum Level{
        INFO, ERROR, WARNING, SUCCESS
    }
}

