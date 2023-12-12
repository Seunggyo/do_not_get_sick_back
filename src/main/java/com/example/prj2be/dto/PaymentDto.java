package com.example.prj2be.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NonNull
    private Long amount;
    @NonNull
    private String paymentName;
    @NonNull
    private String email;
    private String status = "CASH";
    private String paymentUid;
    private String successUrl;
    private String failUrl;

}
