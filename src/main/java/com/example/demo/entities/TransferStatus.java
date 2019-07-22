package com.example.demo.entities;

public enum TransferStatus {
    PENDING("PENDING"),
    COMPLETED("COMPLETED");

    private final String value;

    TransferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
