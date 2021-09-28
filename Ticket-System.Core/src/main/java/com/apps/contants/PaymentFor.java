package com.apps.contants;

import lombok.Getter;

@Getter
public enum PaymentFor {
    TICKET("Ticket"),
    GIFT("Gift");

    PaymentFor(String value) {
        this.value = value;
    }

    private String value;
}
