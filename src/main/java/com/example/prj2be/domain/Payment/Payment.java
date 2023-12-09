package com.example.prj2be.domain.Payment;

import com.example.prj2be.dto.PaymentResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends PaymentResDto {
    private Long id;
    private String status;
    private Long amount;
    private String paymentUid;
    private String paymentName;
    private boolean paySuccessYN;
    private String memberId;
    private String paymentKey;
    private String failReason;
    private boolean cancelYN;
    private String cancelReason;
}
