package com.apps.authenticate.contants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public enum MessageData implements Serializable {
    NOT_FOUND_ACCOUNT_STATUS("Not Found ",HttpStatus.NOT_FOUND);

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

