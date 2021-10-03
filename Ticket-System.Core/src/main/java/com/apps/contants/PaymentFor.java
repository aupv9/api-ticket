package com.apps.contants;

import lombok.Getter;

@Getter
public enum PaymentFor {
    TICKET("Orders"),
    GIFT("Gift");

    PaymentFor(String value) {
        this.value = value;
    }

    private String value;
}
