package com.example.prj2be.exception;

public enum ExceptionCode {
    INVALID_PAYMENT_AMOUNT("최소 주문 금액은 1000원 입니다."),
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다"),
    PAYMENT_AMOUNT_EXP("값이 일치하지 않습니다"),
    ALREADY_APPROVED("이미 처리되었습니다");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
