package com.github.ku4marez.payment.service;

import com.github.ku4marez.payment.dto.ExternalPaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PaymentGatewayClient {

    private final WebClient webClient = WebClient.builder()
        .baseUrl("http://fake-stripe-mock:8080")
        .build();

    @Retry(name = "paymentRetry")
    @CircuitBreaker(name = "paymentCircuitBreaker")
    @RateLimiter(name = "paymentRateLimiter")
    @TimeLimiter(name = "paymentTimeLimiter")
    public CompletableFuture<ExternalPaymentResponse> createPayment(String orderId, BigDecimal amount) {

        return webClient.post()
            .uri("/payments")
            .bodyValue(Map.of("orderId", orderId, "amount", amount))
            .retrieve()
            .bodyToMono(ExternalPaymentResponse.class)
            .toFuture();
    }
}
