package com.example.pantara.service;

import com.example.pantara.dto.request.LoginRequest;
import com.example.pantara.dto.request.RegistrationRequest;
import com.example.pantara.dto.request.PasswordResetRequest;
import com.example.pantara.dto.request.OtpVerificationRequest;
import com.example.pantara.dto.response.AuthResponse;

public interface AuthService {
    void register(RegistrationRequest request);
    AuthResponse login(LoginRequest request);
    void sendPasswordResetOtp(String email);
    void resetPassword(PasswordResetRequest request);
    void verifyEmail(OtpVerificationRequest request);
    void resendOtp(String email, String type);
    void logoutAllDevices(String userId);
    void cleanupExpiredTokens();
}