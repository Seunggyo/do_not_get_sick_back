package com.example.prj2be.service.payment;

import com.example.prj2be.config.PaymentConfig;
import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.dto.PaymentDto;
import com.example.prj2be.dto.PaymentResDto;
import com.example.prj2be.dto.PaymentSuccessDto;
import com.example.prj2be.exception.CustomLogicException;
import com.example.prj2be.exception.ExceptionCode;
import com.example.prj2be.mapper.drug.CartMapper;
import com.example.prj2be.mapper.member.MemberMapper;
import com.example.prj2be.mapper.order.OrderListMapper;
import com.example.prj2be.mapper.order.OrderWaitMapper;
import com.example.prj2be.mapper.order.OrdersMapper;
import com.example.prj2be.mapper.payment.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final MemberMapper memberMapper;
    private final PaymentConfig paymentConfig;
    private final EntityService entityService;
    private final OrderWaitMapper orderWaitMapper;
    private final OrdersMapper ordersMapper;
    private final OrderListMapper orderListMapper;
    private final CartMapper cartMapper;


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

    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount, Member login) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);
        payment.setPaySuccessYN(true);

        oders(orderId, login.getId());

        return result;
    }

    public Payment verifyPayment(String orderId, Long amount) {
        if (paymentMapper.findByUid(orderId) != null ) {
            Payment payment = paymentMapper.findByUid(orderId);

            if (!payment.getAmount().equals(amount)) {
                throw new CustomLogicException(ExceptionCode.PAYMENT_AMOUNT_EXP);
            }
            return payment;
        } else {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        }

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
            result = restTemplate.postForObject(paymentConfig.URL,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class);
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

    public void oders(String orderId, String memberId) {
        if (ordersMapper.insert(
                orderWaitMapper.seletByOrderId(orderId)
        ) == 1) {
            List<Cart> cartList = cartMapper.selectCartList(memberId);
            for (Cart cart : cartList) {
                orderListMapper.insertByCart(cart, orderId);
            }

            cartMapper.deleteByMemberId(memberId);
            orderWaitMapper.deleteByOrderId(orderId);


        }
    }
}
