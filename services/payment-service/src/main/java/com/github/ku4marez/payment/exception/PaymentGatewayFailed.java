package com.github.ku4marez.payment.exception;

import com.github.ku4marez.ecom.starters.web.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PaymentGatewayFailed extends ApiException {
    public PaymentGatewayFailed() {
        super("Payment gateway failed", HttpStatus.BAD_GATEWAY);
    }
}
