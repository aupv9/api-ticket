package com.apps.contants;

import lombok.Getter;

@Getter
public enum AuthorityEnum {
    READ_LOCATION("READ_LOCATION");

    private final String value;

    AuthorityEnum(String value) {
        this.value = value;
    }
}
