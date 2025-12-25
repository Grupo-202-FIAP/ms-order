package com.nextime.order.domain.enums;

public enum PaymentStatus {
    PROCESSED("PROCESSED"),
    UNKNOWN("UNKNOWN"),
    EXPIRED("EXPIRED");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
