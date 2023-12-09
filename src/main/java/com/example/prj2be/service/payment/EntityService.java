package com.example.prj2be.service.payment;

import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.dto.PaymentDto;
import com.example.prj2be.mapper.payment.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EntityService {

    private final PaymentMapper paymentMapper;


    public Payment toEntity(PaymentDto paymentDto) {
        paymentMapper.insertByDTO(paymentDto);
        return paymentMapper.selectByDTO(paymentDto);
    }
}
