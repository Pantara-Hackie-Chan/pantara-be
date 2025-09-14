package com.example.pantara.exception;

/**
 * Exception thrown when rate limits are exceeded
 */
public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(String operation, String timeWindow) {
        super(
                "RATE_LIMIT_EXCEEDED",
                "Rate limit exceeded for %s. Please try again after %s".formatted(operation, timeWindow),
                operation, timeWindow
        );
    }

    public static RateLimitExceededException otpRequests(String email, String timeWindow) {
        return new RateLimitExceededException(
                "OTP requests for " + email,
                timeWindow
        );
    }
}