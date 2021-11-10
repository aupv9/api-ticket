package com.apps.contants;

import lombok.Getter;

@Getter
public enum Role {
    STAFF("ROLE_Manager_Theater"),
    USER("Role_User"),
    MANAGER("ROLE_Manager_Theater"),
    DIRECTOR("ROLE_DIRECTOR")

    ;

    Role(String name) {
        this.name = name;
    }

    private String name;
}
