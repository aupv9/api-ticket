package com.apps.contants;

import lombok.Getter;

@Getter
public enum PaymentFor {
    TICKET("Orders"),
    GIFT("Gift"),
    MEMBER_CASH("MemberCash");

    PaymentFor(String value) {
        this.value = value;
    }

    private String value;
}
