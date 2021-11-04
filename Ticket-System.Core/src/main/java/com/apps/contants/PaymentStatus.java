package com.apps.contants;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    Pending("Pending"),
    Verified("Verified"),
    Complete("Complete");

    PaymentStatus(String value) {
        this.value = value;
    }

    private String value;
}
