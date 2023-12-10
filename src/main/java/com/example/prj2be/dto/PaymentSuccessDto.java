package com.example.prj2be.dto;

import lombok.Data;

@Data
public class PaymentSuccessDto {
    String mid;
    String version;
    String paymentKey;
    String orderId;
    String orderName;
    String currency;
    String method;
    String totalAmount;
    String balanceAmount;
    String suppliedAmount;
    String vat;
    String status;
    String requestedAt;
    String approvedAt;
    String useEscrow;
    String cultureExpense;
    String type;
}
