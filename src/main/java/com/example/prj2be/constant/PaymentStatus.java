package com.example.prj2be.constant;

public enum PaymentStatus {
    CARD("카드"),CASH("현금");
    private String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
