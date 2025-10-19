package com.github.ku4marez.ecom.starters.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class ApiErrorHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        log.info("API exception: {} -> {}", ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
            .body(Map.of(
                "error", ex.getClass().getSimpleName(),
                "message", ex.getMessage(),
                "status", ex.getStatus().value()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> generic(Exception ex) {
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", ex.getClass().getSimpleName(),
                "message", ex.getMessage(),
                "status", 500));
    }
}
