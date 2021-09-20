package com.apps.contants;

import lombok.Getter;


@Getter
public enum StatusSeat {
    PENDING("Pending"),
    AVAILABLE("Available"),
    SOLID("Solid")

    ;


    StatusSeat(String status) {
        this.status = status;
    }

    private String status;
}
