package com.example.prj2be.dto;

import lombok.Data;

@Data
public class PaymentRequsetDto {
    private String paymentKey;
    private String paymentUid;
    private Long amount;
}
