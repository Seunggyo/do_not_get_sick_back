package com.example.prj2be.service.payment;

import com.example.prj2be.config.PaymentConfig;
import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.dto.PaymentDto;
import com.example.prj2be.dto.PaymentResDto;
import com.example.prj2be.exception.CustomLogicException;
import com.example.prj2be.exception.ExceptionCode;
import com.example.prj2be.mapper.member.MemberMapper;
import com.example.prj2be.mapper.payment.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final MemberMapper memberMapper;
    private final PaymentConfig paymentConfig;


    public Payment requsetTossPayment(Payment payment, String email) throws CustomLogicException {
        if (payment.getAmount() < 1000) {
            throw new CustomLogicException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
        payment.setMemberId(memberMapper.findIdByEmail(email));
        paymentMapper.updateMemberIdByPayment(payment);
        return payment;
    }
}
