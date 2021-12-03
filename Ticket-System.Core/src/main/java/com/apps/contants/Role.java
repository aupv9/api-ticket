package com.apps.contants;

import lombok.Getter;

@Getter
public enum Role {
    STAFF("ROLE_STAFF"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    SENIOR_MANAGER("ROLE_SENIOR_MANAGER"),
    ADMIN("ADMIN")
    ;

    Role(String name) {
        this.name = name;
    }

    private String name;
}
