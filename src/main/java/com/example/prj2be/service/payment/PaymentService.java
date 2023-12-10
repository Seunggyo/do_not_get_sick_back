package com.example.prj2be.service.payment;

import com.example.prj2be.config.PaymentConfig;
import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.dto.PaymentDto;
import com.example.prj2be.dto.PaymentResDto;
import com.example.prj2be.dto.PaymentSuccessDto;
import com.example.prj2be.exception.CustomLogicException;
import com.example.prj2be.exception.ExceptionCode;
import com.example.prj2be.mapper.member.MemberMapper;
import com.example.prj2be.mapper.payment.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final MemberMapper memberMapper;
    private final PaymentConfig paymentConfig;
    private final EntityService entityService;


    public PaymentResDto requsetTossPayment(PaymentDto paymentDto, String email) throws CustomLogicException {

        Payment payment = entityService.toEntity(paymentDto);
        if (payment.getAmount() < 1000) {
            throw new CustomLogicException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
        payment.setMemberId(memberMapper.findIdByEmail(email));
        paymentMapper.updateMemberIdByPayment(payment);

        PaymentResDto paymentResDto = new PaymentResDto();

        paymentResDto.setStatus(payment.getStatus());
        paymentResDto.setAmount(payment.getAmount());
        paymentResDto.setPaymentName(payment.getPaymentName());
        paymentResDto.setPaymentName(payment.getPaymentName());
        paymentResDto.setPaymentUid(payment.getPaymentUid());
        paymentResDto.setCustomerEmail(email);
        paymentResDto.setCustomerName(payment.getMemberId());
        return paymentResDto;
    }

    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);

        return result;
    }

    public Payment verifyPayment(String orderId, Long amount) {
        Payment payment = paymentMapper.findByUid(orderId).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
        if (payment.getAmount().equals(amount)) {
            throw new CustomLogicException(ExceptionCode.PAYMENT_AMOUNT_EXP);
        }
        return payment;
    }

    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);

        PaymentSuccessDto result = null;
        try {
            System.out.println("-------------");
            result = restTemplate.patchForObject(paymentConfig.URL,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class);
            System.out.println("---1-1-1-1-1-1-");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomLogicException(ExceptionCode.ALREADY_APPROVED);
        }
        return result;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodeAuthKey = new String(
                Base64.getEncoder().encode((paymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8))
        );
        headers.setBasicAuth(encodeAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
