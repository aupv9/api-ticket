package com.apps.contants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("ordered"),
    CANCELLED("cancelled"),
    NON_PAYMENT("non_payment"),
    EDITED("EDITED")
    ;

    OrderStatus(String status) {
        this.status = status;
    }

    private String status;
}
