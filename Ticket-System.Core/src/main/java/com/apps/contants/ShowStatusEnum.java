package com.apps.contants;

import lombok.Getter;

@Getter
public enum ShowStatusEnum {
    New("New"),
    Now("Now Playing")
    ,Soon("Soon"),
    Expire("Expire");
    private final String name;

    ShowStatusEnum(String name) {
        this.name = name;
    }
}
