package com.apps.contants;

import lombok.Getter;

@Getter
public enum UserStatus {
    WAIT_CONFIRM("WAIT_CONFIRM"),
    ACTIVE("ACTIVE"),
    NEW("NEW"),
    BLOCKED("BLOCKED")

    ;
    private final String name;

    UserStatus(String name) {
        this.name = name;
    }
}
