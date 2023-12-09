package com.example.prj2be.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PaymentConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String testClientApikey;
    @Value("${payment.toss.test_secret_api_key}")
    private String testSecretKey;
    @Value("${payment.toss.success_url}")
    private String successUrl;
    @Value("${payment.toss.fail_url}")
    private String failUrl;
    public static final String URL = "https://api.tosspayments.com/v1/payments/confirm";
}